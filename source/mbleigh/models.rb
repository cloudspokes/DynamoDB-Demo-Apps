class User
  attr_reader :item

  def self.create(name)
    User.new(TABLES['users'].items.create(id: name))
  end

  def self.all
    TABLES['users'].items.to_a.map{|i| User.new(i)}
  end

  def following?(other)
    @item.attributes['following'].to_a.include?(other.name)
  end

  def initialize(item)
    @item = item
  end

  def tweet(text)
    Tweet.create(self, text)
  end

  def distribution
    ['global', name] + @item.attributes['followers'].to_a
  end

  def name
    @item.hash_value
  end

  def follow(other)
    @item.attributes.add(following: [other])
    User[other].item.attributes.add(followers: [self.name])
  end

  def unfollow(other)
    @item.attributes.delete(following: [other])
    User[other].item.attributes.delete(followers: [self.name])
  end

  def self.[](name)
    User.new(TABLES['users'].items.at(name))
  end
end

class Tweet
  def self.create(user, text)
    now = Time.now
    user.distribution.each do |u|
      TABLES['tweets'].items.create(timeline_id: u, created_at: now.to_i, text: text, user: user.name)
    end
  end

  def self.timeline(name)
    return [] if name.nil? || name == ''
    TABLES['tweets'].items.query(hash_value: name).to_a.map{|i| Tweet.new(i)}.reverse
  end

  attr_reader :item
  def initialize(item); @item = item end
  def text; item.attributes['text'] end
  def user; item.attributes['user'] end
end