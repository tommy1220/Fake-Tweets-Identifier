$(document).on('change', 'input:radio[id^="option"]', function (event) {

    var tweetId = $('#tweetId').val();
    var vote = $(this).val();
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'POST',
        data: '{"tweetId":"' + tweetId + '","vote":"' + vote + '"}',
        url: '/recordpoll',
        success: function(res){
            $('div.poll').html('<h4> Thank you for your vote </h4>');
        },
        error: function(res){
            $('div.poll').html('<h4> Error in recording vote </h4>');
        }
    });
});

$('#tellmemore').click(function() {
    $('#morecontents').slideToggle();
    $(this).text(function(i, text){
        return text === "SHOW DETAILED TWEET STATISTICS" ? "HIDE DETAILED TWEET STATISTICS" : "SHOW DETAILED TWEET STATISTICS";
    })
});