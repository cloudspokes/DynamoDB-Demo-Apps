class SurveysController < ApplicationController
  
  def index
    # list of survey
    @surveys = Survey.all

  end

  def show
    # get all surveys
    @surveys = Survey.all
    
    # selected survey
    @survey = Survey.find(params[:id])    # id is a survey title
    
    # save survey submit
    if params[:question]
      if !params[:username].nil?
        username = params[:username]
      else
        # just set random username
        i = (rand *1000).round
        username = 'user'+i.to_s
      end
      reply = Result.new(:surveytitle => params[:id], :username => username, :reply => params[:question])
      reply.save
    end

    # get the updated result
    @results = Result.query(:hash_value => params[:id])   # array of Result
    
    @tally = {:q1 => 0, :q2 => 0, :q3 => 0, :q4 => 0, :q5 => 0}
    @results.each do |r|
      @tally[r.reply.to_sym] = @tally[r.reply.to_sym] + 1
    end
    logger.info "#{p @tally}"
    @tally.each do |k, v|
      @tally[k] = ((v.to_f / @results.size) * 100).round()
    end
    # logger.info "#{p @tally}"
  end
  
end
