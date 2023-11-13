$(window).scroll(function(){
    var header = $('.header');

    if(header.offset().top !==0){
      if(!header.hasClass('shadow')){
        header.addClass('shadow');
      }
    } else {
      header.removeClass('shadow');
    }
});