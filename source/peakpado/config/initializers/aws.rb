
require 'aws'

def dynamo_client
  if $dynamo_client.nil?
    # AWS.config(:logger => Rails.logger)
    # load credentials from a file
    config_path = File.expand_path(File.dirname(__FILE__)+"/../aws.yml")
    AWS.config(YAML.load(File.read(config_path)))
         
    $dynamo_client = AWS::DynamoDB.new()
  end
  $dynamo_client
end

dynamo_client
