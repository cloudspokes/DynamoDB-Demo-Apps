$:.unshift File.dirname(__FILE__) + '/../lib'

require 'rspec'
require 'dynamo_connection'

describe DynamoConnection do
  before :each do
    # We don't actually want to touch AWS while testing
    DynamoConnection.stub!(:instance).and_return(double) 
  end

  it "has a singleton instance" do
    DynamoConnection.instance.object_id.should == DynamoConnection.instance.object_id
  end

end