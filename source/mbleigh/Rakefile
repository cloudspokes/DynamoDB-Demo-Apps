task "Load the environment"
task :environment do
  require File.dirname(__FILE__) + '/environment'
end

desc "Set up the DynamoDB tables etc. for you"
task setup: :environment