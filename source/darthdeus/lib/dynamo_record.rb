$:.unshift File.dirname(__FILE__)
require 'dynamo_connection'
require 'uuidtools'
class DynamoRecord

  class TableNotFoundException < Exception; end

  class << self
    def table_name(name = nil)
      @name = name if name

      if @name
        return @name
      else
        # Dead simple pluralization
        (self.to_s + "s").downcase
      end
    end

    # Establish a connection to the database
    def establish_connection
      @db = DynamoConnection.instance
      @table_name = table_name
      # Initialize the schema
      begin
        @table = @db.tables[@table_name]
        @table.status
        @connected = true
      rescue AWS::DynamoDB::Errors::ResourceNotFoundException
        raise DynamoRecord::TableNotFoundException
      end
    end

    def all
      self.establish_connection unless @connected
      @table.items.to_a
    end

    def find(id)
      self.establish_connection unless @connected
      puts "Loading for id: #{id}"
      @table.items.where(:id => BigDecimal.new(id)).first
    end

    def find_instance(id)
      new(find(id))
    end

    def create(params)
      self.establish_connection unless @connected
      # We need to convert the UUID to BigDecimal to fit in the ID column,
      # there is probably a better way to do this
      uuid = BigDecimal.new(UUIDTools::UUID.timestamp_create.to_s)
      @table.items.create({:id => uuid}.merge(params))
    end
  end

  def initialize(object)
    @object = object
    @attributes = {}
  end

  def table_name=(name)
    @table_name = name
  end

  def table_name
    return @table_name.to_s if @table_name
    self.class.table_name.to_s
  end

  def []=(name, value)
    @object.attributes[name] = value
  end

  def [](name)
    @object.attributes[name]
  end  
end
