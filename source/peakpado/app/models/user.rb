#
# table name: User
# hash_key: name, string
#

class User < DynamoHash

  validates :name, :presence => true
  
  def self.table_name
    'User'
  end
  
  def hash_key
    'name'
  end

  # Without this validation gets method_missing
  def name
    @attributes[:name.to_s]
  end
  
end
