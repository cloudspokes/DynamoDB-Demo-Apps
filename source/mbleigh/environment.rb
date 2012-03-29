$:.push File.dirname(__FILE__)

require 'bundler'
Bundler.require :default, (ENV['RACK_ENV'] || 'development')

require 'aws'
AWS.config(
  access_key_id: ENV['AWS_KEY'],
  secret_access_key: ENV['AWS_SECRET']
)

DB = AWS::DynamoDB.new
TABLES = {}

{'tweets' => {hash_key: {timeline_id: :string}, range_key: {created_at: :number}},
 'users' => {hash_key: {id: :string}}}.each_pair do |table_name, schema|
  begin
    TABLES[table_name] = DB.tables[table_name].load_schema
  rescue AWS::DynamoDB::Errors::ResourceNotFoundException
    table = DB.tables.create(table_name, 10, 5, schema)
    print "Creating table #{table_name}..."
    sleep 1 while table.status == :creating
    print "done!\n"
    TABLES[table_name] = table.load_schema
  end
end

require 'models'