$:.unshift File.dirname(__FILE__) + '/lib'

require 'aws-sdk'
require 'sinatra'
require 'dynamo_record'
require 'slim'
require 'json'

class Task < DynamoRecord
end

get '/' do
  @tasks = Task.all

  slim :index
end

get '/get/:id' do
  task = Task.find(params[:id])
  halt 404, "Task not found" unless task
  
  content_type :json
  data = task.attributes.to_h
  # Convert tags to array and join back together
  data['tags'] = [*data['tags']].join(", ")
  data.to_json
end

post '/update/:id' do  
  task = Task.find_instance(params['id'])

  task['name'] = params['name']
  task['tags'] = params['tags'].split(",").map(&:strip)

  content_type :json
  params.to_json
end

post '/create' do
  task = Task.create(:name => params['name'], :tags => params['tags'].split(",").map(&:strip))

  content_type :json  
  hash = task.attributes.to_h

  # Tags are store as a Set, so we need to convert it to an Array before we join
  { :id => hash['id'], :name => hash['name'], :tags => hash['tags'].to_a.join(", ") }.to_json
end

post '/delete/:id' do
  task = Task.find(params[:id])
  task.delete 

  content_type :json
  {}.to_json
end

get '/style.css' do
  scss :style
end