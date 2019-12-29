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
                        linkColor: '#cc0000', // default is blue
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
		<div
			class="cover-container d-flex w-100 h-100 p-3 mx-auto flex-column">
			<header class="masthead mb-auto">
				<div class="inner"></div>
			</header>
			<main role="main" class="inner cover"> <a href="/"> <img
				src="images/logo_file.png" alt="logo" class="cover-heading" />
			</a> <br />
			<p class="lead">
				<form:form method="POST" action="/" name="tweetForm">
					<div class="input-group">
						<input type="text"
							pattern="http(?:s)?:\/\/(?:www\.)?twitter\.com\/(.*)\/status\/(\d+)(\/*.*)"
							class="form-control input-lg" id="url" name="url"
							placeholder="Enter a Valid Tweet URL"
							title="Please specify a valid tweet status url" value="${url}"
							required /> <span class="input-group-btn">
							<button class="btn btn-default" type="submit">
								<i class="fa fa-search"></i>
							</button>
						</span>
					</div>
					<c:choose>
						<c:when test="${not empty error}">
							<div class="row center-block">
								<div class="alert alert-danger alert-dismissible">
									<a href="#" class="close" data-dismiss="alert"
										aria-label="close">&times;</a> <strong>Issues found
										in analyzing tweet: </strong> ${error}
								</div>
							</div>
						</c:when>
					</c:choose>
				</form:form>
			</p>
			<c:choose>
				<c:when test="${empty j48PredictedClass}">
					<span>
						<p class="lead">
							We process the tweet and its media entities. We run it against
							machine learning models in our system trained using two machine
							learning algorithms <b>J48</b> and <b>SVM</b> and classify it as
							<b>FAKE</b> or <b>REAL</b>.
						</p>
					</span>
				</c:when>
				<c:otherwise>
					<p class="lead">
						According to our system's model trained with <b>J48</b> algorithm
						it's <b>${fn:toUpperCase(j48PredictedClass)}</b>
					</p>
					<p class="lead">
						According to our system's model trained with <b>SVM</b> algorithm
						it's <b>${fn:toUpperCase(svmPredictedClass)}</b>
					</p>
					<p class="lead">${fakeCount}people have voted its FAKE and
						${realCount} have voted its REAL</p>
					<div id="poll" name="poll" class="poll">
						<h4>What do you think?</h4>
						<div class="btn-group btn-group-toggle" data-toggle="buttons">
							<label class="btn btn-secondary active"> <input
								type="radio" name="options" id="option1" value="REAL"
								autocomplete="off"> REAL
							</label> <label class="btn btn-secondary"> <input type="radio"
								name="options" id="option2" value="FAKE" autocomplete="off">
								FAKE
							</label>
						</div>
					</div>
					<br />
					<p class="lead">
						<%--<a id="tellmemore" class="btn btn-md btn-secondary">Show detailed tweet statistics</a--%>
						<a id="tellmemore" class="btn btn-md btn-warning">SHOW
							DETAILED TWEET STATISTICS</a>
					</p>
				</c:otherwise>
			</c:choose>
			<div id="morecontents" name="morecontents" style="display: none">
				<c:choose>
					<c:when test="${not empty j48PredictedClass and empty error}">
						<div class="row">
							<div class="col-lg-12 portfolio-item">
								<div class="card h-100">
									<div class="card-body">
										<h4 class="card-title">
											<a href="#">Tweet Credibility</a>
										</h4>
										<p class="card-text">
										<ul class="text-left">

											<c:choose>
												<c:when
													test="${(featurevector.isUrlCredible == 1 and featurevector.containsUrl)}">
													<li>The URL's in the tweet content are <b>credible</b></li>
												</c:when>
												<c:when
													test="${featurevector.isUrlCredible == 0 and featurevector.containsUrl}">
													<li style="color: red">The URL's in the tweet content
														does <b>NOT</b> look credible
													</li>
												</c:when>
											</c:choose>

											<c:choose>
												<c:when
													test="${featurevector.isImageCredible == 1 and featurevector.containsImages}">
													<li>The images in the tweet content are <b>credible</b></li>
												</c:when>
												<c:when
													test="${featurevector.isImageCredible == 0 and featurevector.containsImages}">
													<li style="color: red">The images in the tweet content
														does <b>NOT</b> look credible
													</li>
												</c:when>
											</c:choose>

											<c:choose>
												<c:when
													test="${featurevector.doesGoogleDocumentSearchForMediaEntityHaveFakeUrl and featurevector.containsImages}">
													<li style="color: red">On doing a google document
														search for media entities in tweet, <b>there were
															media entities from known list of fake url's</b>
													</li>

												</c:when>
												<c:when
													test="${ (not featurevector.doesGoogleDocumentSearchForMediaEntityHaveFakeUrl) and featurevector.containsImages}">
													<li>On doing a google document search for media
														entities in tweet, <b>there were NO media entities
															from known list of fake url's</b>
													</li>
												</c:when>
											</c:choose>
											<c:choose>
												<c:when
													test="${featurevector.doesGoogleImageSearchForMediaEntityHaveFakeUrl and featurevector.containsImages}">
													<li style="color: red">On doing an <i>image</i> search
														for media entities in tweet, <b>there were media
															entities from known list of fake url's</b></li>

												</c:when>
												<c:when
													test="${ (not featurevector.doesGoogleImageSearchForMediaEntityHaveFakeUrl) and featurevector.containsImages}">
													<li>On doing a google <i>image</i> search for media
														entities in tweet, <b>there were NO media entities
															from known list of fake url's</b></li>
												</c:when>
											</c:choose>

											<c:choose>
												<c:when
													test="${(featurevector.doesGoogleImageSearchForMediaEntityHaveWordFakeInDescription || featurevector.doesGoogleImageSearchForMediaEntityHaveWordFakeInTitle)  and featurevector.containsImages}">
													<li style="color: red">On doing a google image search
														for media entities in tweet, <b>there were search
															results that indicate the tweet is fake</b>
													</li>
												</c:when>
												<c:when
													test="${((not featurevector.doesGoogleImageSearchForMediaEntityHaveWordFakeInDescription) || (not featurevector.doesGoogleImageSearchForMediaEntityHaveWordFakeInTitle))  and featurevector.containsImages}">
													<li>On doing a google image search for media entities
														in tweet, <b>there were NO search results that
															indicate the tweet is fake</b>
													</li>
												</c:when>
											</c:choose>
											<c:choose>
												<c:when
													test="${featurevector.doesGoogleImageSearchForMediaEntityHaveDateMisaligned  and featurevector.containsImages}">
													<li style="color: red">On doing a google image search
														for media entities in tweet, <b>there were search
															results with same content much ahead of time than the
															incident</b>
													</li>
												</c:when>
												<c:when
													test="${(not featurevector.doesGoogleImageSearchForMediaEntityHaveDateMisaligned)  and featurevector.containsImages}">
													<li>On doing a google image search for media entities
														in tweet, <b>there were NO search results with same
															content much ahead of time than the incident</b>
													</li>
												</c:when>
											</c:choose>
											<c:choose>
												<c:when
													test="${featurevector.bestGuessForGoogleImageSearchForMediaEntityUnrelated  and featurevector.containsImages}">
													<li style="color: red">The best guess result of google
														image search for media entities in tweet seems <b>unrelated</b>
													</li>
												</c:when>
												<c:when
													test="${(not featurevector.bestGuessForGoogleImageSearchForMediaEntityUnrelated)  and featurevector.containsImages}">
													<li>The best guess result of google image search for
														media entities in tweet seems <b>related</b>
													</li>
												</c:when>

											</c:choose>
										</ul>
										</p>
									</div>
								</div>
							</div>
							<div class="col-lg-12 portfolio-item">
								<br>
							</div>

							<div class="col-lg-12 portfolio-item">
								<div class="card h-100">
									<div class="card-body">
										<h4 class="card-title">
											<a href="#">Sentiment Analysis</a>
										</h4>
										<p class="card-text text-left">
											The sentiment score of the tweet is
											${featurevector.sentimentalScore} where <b>-1</b> indicate
											the tone of the tweet is most <b>negative</b> and <b>+1 </b>indicates
											tone of the tweet being <b>extremely positive</b>
										</p>
									</div>
								</div>
							</div>
							<div class="col-lg-12 portfolio-item">
								<br>
							</div>
							<div class="col-lg-12 portfolio-item">
								<div class="card h-100">
									<div class="card-body">
										<h4 class="card-title">
											<a href="#">Twitter account statistics</a>
										</h4>
										<p class="card-text">
										<table class="table text-left">

											<tbody>
												<%--<th>TWEET USER ANALYSIS</th>--%>
												<tr class="success">
													<td>Tweet user Friends count</td>
													<td>${featurevector.noOfFriends}</td>
												</tr>
												<tr class="success">
													<td>Tweet user Followers Count</td>
													<td>${featurevector.noOfFollowers}</td>
												</tr>
												<tr class="success">
													<td>Tweet user Friend Follower Ratio</td>
													<td>${featurevector.friendFollowerRatio}</td>
												</tr>
												<tr class="success">
													<td>Does the tweet user have URL?</td>
													<td>${featurevector.isUserHasURL == 1  ? 'Yes' : 'No'}</td>
												</tr>
												<tr class="success">
													<td>Is the user account verified?</td>
													<td>${featurevector.isVerifiedUser == 1  ? 'Yes' : 'No'}</td>
												</tr>
												<tr class="success">
													<td>Number of tweets user has posted</td>
													<td>${featurevector.noOfTweets}</td>
												</tr>
											</tbody>
										</table>
										</p>
									</div>
								</div>
							</div>
						</div>
					</c:when>
				</c:choose>
				<input type="hidden" id="tweetId" name="tweetId" class="tweetId"
					value="${tweetId}">
			</div>
			</main>
			<footer class="mastfoot mt-auto">
				<div class="inner">
					<p>&copy; University of Washington Bothell</p>
				</div>
			</footer>
		</div>
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