require File.dirname(__FILE__) + '/environment'
require 'sinatra'

use Rack::Session::Cookie

helpers do
  def current_user
    return nil if session[:user].nil?
    u = User[session[:user]]
    return nil unless u.item.exists?
    u
  end

  def logged_in?
    !!current_user
  end
end

get '/' do
  erb :home
end

post '/sign_in' do
  session[:user] = params[:username]
  session[:user] = nil if session[:user] == ''

  if session[:user]
    u = User[session[:user]]
    unless u.item.exists?
      u = User.create(session[:user])
    end
  end

  redirect '/'
end

post '/tweet' do
  User[session[:user]].tweet(params[:text])
  redirect '/'
end

post '/follow' do
  User[session[:user]].follow(params[:username])
  redirect '/'
end

get '/styles.css' do
  sass :styles
end
