<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">
<title>Fake Tweet Identifier</title>
<script sync src="https://platform.twitter.com/widgets.js"></script>
<script>
        window.onload = (function () {
            var tweet = document.getElementById("tweet");
            var id = tweet.getAttribute("tweetID");

            twttr.widgets.createTweet(
                    id, tweet,
                    {
                        conversation: 'none',    // or all
                        cards: 'visible',  // or visible
                        linkColor: 'blue', // default is blue
                        theme: 'light'    // or dark
                    })
                    .then(function (el) {
                        //el.contentDocument.querySelector(".footer").style.display = "none";
                    });

        });

    </script>
<!-- Bootstrap core CSS -->

<!-- Custom styles for this template -->
<link href="css/cover.css" rel="stylesheet">
<spring:url value="css/faketweetidentifier.css" var="coreCss" />
<spring:url value="css/bootstrap.min.css" var="bootstrapCss" />
<link href="${bootstrapCss}" rel="stylesheet" />
<link href="${coreCss}" rel="stylesheet" />
<link
	href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"
	rel="stylesheet">
</head>
<body>
	<div class="container h-100 d-flex text-center">
		<input type="hidden" id="tweetId" name="tweetId" class="tweetId"
			value="${tweetId}">
	</div>
	<c:choose>
		<c:when test="${not empty tweetId}">
			<div id="tweet" name="tweet" tweetID="${tweetId}"></div>
		</c:when>
	</c:choose>

	<!-- Bootstrap core JavaScript
================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>

	<script src="js/popper.min.js"></script>
	<script src="js/bootstrap.min.js"></script>

	<spring:url value="js/faketweetidentifier.js" var="coreJs" />
	<spring:url value="js/bootstrap.min.js" var="bootstrapJs" />
	<script src="${coreJs}"></script>
	<script src="${bootstrapJs}"></script>
</body>
</html>