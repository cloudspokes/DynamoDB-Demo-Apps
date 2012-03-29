# AWS DynamoDB
#
# Base class
#

class DynamoDB

  include ActiveModel::Validations  # valid?

  def initialize(attrs={})
    # attrs include keys
    @attributes = {}
    attrs.each do |k, v|
      # every thing from Dynamo is string
      k = k.to_s if k.kind_of? Symbol
      @attributes[k] = v
    end
  end
  
  def to_param
    to_key
  end
  
  def self.table
    t = $dynamo_client.tables[table_name]
    t.load_schema
    t
  end
  
  def table
    self.class.table
  end
  
  def method_missing(method, *args, &block)
    string_name = method.to_s
    if @attributes[string_name]             # read
      @attributes[string_name]
    elsif string_name.end_with?('=')
      string_name.partition("=")[0]
      @attributes[string_name.partition("=")[0]] = args[0]    # write
    else
      super
    end
  end
  
  def self.all
    results = []
    table.items.each do |item|
      results << new(item.attributes.to_h)
    end
    results
  end
  
  # return true or false
  def save
    if exists?
      update_attributes
    else
      if !create.nil?
        true
      else
        false
      end
    end
  end
  
  def destroy
    @item.delete if exists?
    @attributes = {}
  end


  private 
  
  # @attributes already exist
  # return instance
  def create
    @attributes.delete_if { |k, v| v.blank? }
    
    @item = table.items.create(@attributes)
    if @item.exists?
      @attributes = @item.attributes.to_h
      self
    else
      nil
    end
  end
  
end
