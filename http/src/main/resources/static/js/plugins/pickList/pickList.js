(function ($) {

   $.fn.pickList = function (options) {

      var opts = $.extend({}, $.fn.pickList.defaults, options);

      this.fill = function () {
         var option = '';
         var resultOption = '';

         $.each(opts.data, function (key, val) {
        	 if(val.option == "selected"){
        		 resultOption +=  '<option id=' + val.id + '>' + val.text + '</option>';
        	 }else{
        		 option += '<option id=' + val.id + '>' + val.text + '</option>';
        	 }
         });
         this.find('#pickData').append(option);
         this.find("#pickListResult").append(resultOption);
      };
      this.controll = function () {
         var pickThis = this;
         if(opts.add!=null||opts.add!=undefined){
        	 $("#"+opts.add).on('click', function () {
                 var p = pickThis.find("#pickData option:selected");
                 p.clone().appendTo(pickThis.find("#pickListResult"));
                 p.remove();
              }); 
         }
        
         if(opts.addAll!=null||opts.addAll!=undefined){
        	 $("#"+opts.addAll).on('click', function () {
                 var p = pickThis.find("#pickData option");
                 p.clone().appendTo(pickThis.find("#pickListResult"));
                 p.remove();
              });
         }
         if(opts.remove!=null||opts.remove!=undefined){
        	 $("#"+opts.remove).on('click', function () {
                 var p = pickThis.find("#pickListResult option:selected");
                 p.clone().appendTo(pickThis.find("#pickData"));
                 p.remove();
              });
         }
         if(opts.removeAll!=null||opts.removeAll!=undefined){
        	 $("#"+opts.removeAll).on('click', function () {
                 var p = pickThis.find("#pickListResult option");
                 p.clone().appendTo(pickThis.find("#pickData"));
                 p.remove();
              });
         }
      };
      this.getValues = function () {
         var objResult = [];
         this.find("#pickListResult option").each(function () {
            objResult.push(this.id);
         });
         return objResult;
      };
      this.init = function () {
         var pickListHtml =
                 "<div class='row'>" +
                 "  <div class='col-sm-5'>" +
                 "	 <select class='form-control pickListSelect' id='pickData' multiple></select>" +
                 " </div>" +
                 " <div class='col-sm-2 pickListButtons'>";
         if(opts.add!=null||opts.add!=undefined){
        	 pickListHtml += "	<button id="+opts.add+" type='button' class='btn btn-primary btn-sm'><i class='glyphicon glyphicon-chevron-right'></i></button><br/>";
         }
//                 "  <button id='pAddAll' type='button' class='btn btn-primary btn-sm'>" + opts.addAll + "</button>" +
         if(opts.remove!=null||opts.remove!=undefined){
        	 pickListHtml +="	<button id="+opts.remove+" type='button' class='btn btn-primary btn-sm'><i class='glyphicon glyphicon-chevron-left'></i></button>";
         }
//                 "	<button id='pRemoveAll' type='button' class='btn btn-primary btn-sm'>" + opts.removeAll + "</button>" +
         pickListHtml += " </div>" +
                 " <div class='col-sm-5'>" +
                 "    <select class='form-control pickListSelect' id='pickListResult' multiple></select>" +
                 " </div>" +
                 "</div>";

         this.append(pickListHtml);

         this.fill();
         this.controll();
      };

      this.init();
      return this;
   };

   $.fn.pickList.defaults = {
      add: 'Add',
      addAll: 'AddAll',
      remove: 'Remove',
      removeAll: 'RemoveAll'
   };
   $.fn.getValues = function () {
       var objResult = [];
       this.find("#pickListResult option").each(function () {
          objResult.push(this.id);
       });
       return objResult;
    };

}(jQuery));


