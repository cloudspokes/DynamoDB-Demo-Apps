#
# table name: Survey
# hash_key: name, string
#

class Survey < DynamoHash

  validates :title, :presence => true

  def self.table_name
    'Survey'
  end
  
  def hash_key
    'title'
  end
  
  # Without this validation gets method_missing
  def title
    @attributes[:title.to_s]
  end

  
end
