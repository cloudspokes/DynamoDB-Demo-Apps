
$(function() {
  $.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
      if (o[this.name] !== undefined) {
        if (!o[this.name].push) {
          o[this.name] = [o[this.name]];
        }
        o[this.name].push(this.value || '');
      } else {
        o[this.name] = this.value || '';
      }
    });
    return o;
  };

  if (console.log == undefined) {
    console.log = function() {};
  }

  $("form button.create").click(function(e) {
    var data = {
      name: $("#name").val(),
      tags: $("#tags").val(),
    }
    
    var id = $("#task_id").val();
    var self = $(this);

    // disable the button so it can't be clicked again until the request is finished
    self.attr("disabled", "disabled");
    
    $.post('/create', data, function(response) {
      // reenable and render new task
      self.removeAttr("disabled");

      // we clone the main parent element to create a new one and fill it with data      
      var clone = $("#first_father").clone();
      // the simplest way to override display: none !important;
      clone.attr("style", "");
      clone.appendTo($("tbody"))
      
      // ID is converted to exponential format, i.e. 0.1E1, which means we need
      // to remove the dot to allow CSS selectors to be used (dot isn't allowed in ID)
      clone.attr("id", response.id.replace('.', '_'));
      clone.children(".name").html(response.name);
      clone.children(".tags").html(response.tags);

      $("form")[0].reset();
    });
    
    e.preventDefault();
  });

  $("form button.update").click(function(e) {
    var data = {
      name: $("#name").val(),
      tags: $("#tags").val(),
    }

    var id = $("#task_id").val();
    var self = $(this);

    // disable the button so it can't be clicked again until the request is finished
    self.attr("disabled", "disabled");

    $.post('/update/' + id, data, function(response) {
      console.log(response);
      self.removeAttr("disabled");

      // ID is converted to exponential format, i.e. 0.1E1, which means we need
      // to remove the dot to allow CSS selectors to be used (dot isn't allowed in ID)
      var task = $("#task" + response.id.replace('.', '_'));
      task.children(".name").html(response.name);
      task.children(".tags").html(response.tags);
    });

    e.preventDefault();
  });

  $("button.delete").live("click", function(e) {
    var parent = $(this).parents('tr').attr('id');
    
    // ID is converted to exponential format, i.e. 0.1E1, which means we need
    // to remove the dot to allow CSS selectors to be used (dot isn't allowed in ID)
    var id = parent.replace("task", "").replace("_", ".");
    var self = $(this);

    self.attr("disabled", "disabled");
    $.post('/delete/' + id, function(data) {
      self.parents('tr').remove();
      console.log("removed", $(parent));
    });
  });

  $("button.edit").live("click", function(e) {
    // ID is converted to exponential format, i.e. 0.1E1, which means we need
    // to remove the dot to allow CSS selectors to be used (dot isn't allowed in ID)
    var id = $(this).parents('tr').attr('id').replace("task", "").replace("_", ".");
    var self = $(this);

    // disable the button so it can't be clicked again until the request is finished
    self.attr("disabled", "disabled");

    $.get('/get/' + id, function(data) {
      $("#task_id").val(data.id);
      $("#name").val(data.name);
      $("#tags").val(data.tags);

      $(".btn.update").show();
      $(".btn.create").removeClass("primary");

      self.removeAttr("disabled");
      console.log("loaded data", data);
    });

    e.preventDefault();
  });

  $("button.update").hide();
});
