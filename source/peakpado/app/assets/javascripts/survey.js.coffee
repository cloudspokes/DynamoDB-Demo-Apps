# Place all the behaviors and hooks related to the matching controller here.
# All this logic will automatically be available in application.js.
# You can use CoffeeScript in this file: http://jashkenas.github.com/coffee-script/

$ ->
  console.log('survey.js')
  $("#question").buttonset();
  $("#submit").button();
  $('.ui-buttonset').find('.ui-widget').css({'font-size': '14px'}).end();
  $('.ui-button-text').css({'line-height': 0.4, 'padding': '0.4em 0.5em'});
  # $('.sclick').click ->
  #   $("#submit").trigger('click');
  # false