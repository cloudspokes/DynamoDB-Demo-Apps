$:.unshift File.dirname(__FILE__) + '/../lib'

require 'rspec'
require 'dynamo_record'

describe DynamoRecord do

  context "table name" do
    before(:each) do
      # We don't actually want to touch AWS while testing
      DynamoConnection.stub!(:instance).and_return(double.as_null_object)
      # Undefine the class and ignore the exception if the class isn't defined yet
      Object.send(:remove_const, :Task) rescue NameError
    end

    it "has a table" do
      class Task < DynamoRecord
        table_name :tasks
      end

      t = Task.new(stub(:attributes => {}))
      t.table_name.should == "tasks"
    end

    it "sets a default table name from the class" do
      class Task < DynamoRecord; end

      t = Task.new(stub(:attributes => {}))
      t.table_name.should == "tasks"
    end

    it "allows to retrieve attributes via hash notation" do
      class Task < DynamoRecord; end

      t = Task.new(stub(:attributes => {}))
      t["name"] = "foobar"
      t["name"].should == "foobar"
    end

    it "allows to override table name per instance basis" do
      class Task < DynamoRecord; end

      t = Task.new(stub(:attributes => {}))
      t.table_name.should == "tasks"
      t.table_name = "bazinga"
      t.table_name.should == "bazinga"
    end
  end

  context "loadin table from AWS" do
    it "raises an exception if table is not found" do
      calm = stub("Existing table")
      calm.should_receive(:status)
      angry = stub("Non-existing table")
      angry.should_receive(:status).and_raise(AWS::DynamoDB::Errors::ResourceNotFoundException)

      tables = stub("Table collection")
      tables.should_receive(:[]).with("tasks").and_return(calm)
      tables.should_receive(:[]).with("tasken").and_return(angry)

      DynamoConnection.stub_chain(:instance, :tables).and_return(tables)

      class Task < DynamoRecord; end

      expect { Task.establish_connection }.not_to raise_exception
      Task.table_name("tasken")
      expect { Task.establish_connection }.to raise_exception(DynamoRecord::TableNotFoundException)
    end
  end
end