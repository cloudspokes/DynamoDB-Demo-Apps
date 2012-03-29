#
# table name: Result
# hash_key: user_name, string
# range_key: survey_name, string
#

class Result < DynamoHashRange

  validates :username, :presence => true
  validates :surveytitle, :presence => true
  
  def self.table_name
    'Result'
  end
  
  def hash_key
    'surveytitle'
  end
  
  def range_key
    'username'
  end

  # Without this validation gets method_missing
  def surveytitle
    @attributes[:surveytitle.to_s]
  end

  def username
    @attributes[:username.to_s]
  end

end