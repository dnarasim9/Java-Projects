<?php
include 'SpellCorrector.php';
//	make	sure	browsers	see	this	page	as	utf-8	encoded	HTML
header('Content-Type:	text/html;	charset=utf-8');
$limit	= 10;
$query	= isset($_REQUEST['q'])?$_REQUEST['q']:false;
$r	= $_REQUEST['r'];
$algo	= isset($_REQUEST['algorithm'])?$_REQUEST['algorithm']:false;
$results	= false;
if ($query)
{


</script>
//EOT;


	//	The	Apache	Solr	Client	library	should	be	on	the	include	path
	//	which	is	usually	most	easily	accomplished	by	placing	in	the
	//	same	directory	as	this	script	(	.	or	current	directory	is	a	default
	//	php	include	path	entry	in	the	php.ini)
	if($r==0){
	$queryarr=explode(" ",$query);

	foreach($queryarr as $value)
	{
		$tempquery .= SpellCorrector::correct($value).' ';

	}

	$tempquery=trim($tempquery," ");

	 //echo strcmp($query,$tempquery) ;


	if(strcmp($query,$tempquery)!=0)
	{
		echo '<html><head></head><body>';
		echo 'Did you mean:  <a href="http://localhost/hw4final.php?q='.$query.'&r=1">'.$query.'</a>?<br>';
		echo 'Showing results for: '.$tempquery;
		echo '</body</html>';
	}

	$query= $tempquery;
	}


	require_once('/home/dheemanth/solr-php-client/Apache/Solr/Service.php');


	$solr	= new Apache_Solr_Service('localhost',8983,'/solr/cinematicfinal');
	//	if	magic	quotes	is	enabled	then	stripslashes	will	be	needed
	if (get_magic_quotes_gpc()	== 1)
	{
			$query	= stripslashes($query);
	}
	//	in	production	code	you'll	always	want	to	use	a	try	/catch	for	any
	//	possible	exceptions	emitted		by	searching	(i.e.	connection
	//	problems	or	a	query	parsing	error)
	try
	{
		if ($algo == "pgrank")
			$results	= $solr->search($query, 0, $limit, array('sort' => 'pagerank desc'));
		else
			$results	= $solr->search($query,	0,	$limit);
	}
	catch (Exception $e)
	{
		//	in	production	you'd	probably	log	or	email	this	error	to	an	admin
		//	and	then	show	a	special	message	to	the	user	but	for	this	example
		//	we're	going	to	show	the	full	exception
		die("<html><head><title>SEARCH	EXCEPTION</title><body><pre>{$e->__toString()}</pre></body></html>");
	}
}
?>

<?php
function &csv_to_array()
{



$csv = file('dict.csv');

foreach($csv as $line) {
    $line = str_getcsv($line);
    $array[$line[1]] = trim($line[0]);
}


//fclose($handle);

return $array;
}


?>

<html>
		<head>
			<title>PHP Solr Client</title>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
    <link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/themes/smoothness/jquery-ui.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"></script>
    <script src="http://code.jquery.com/jquery-2.1.1.min.js"></script>
<script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.9/jquery.validate.min.js"></script>
 <script src="//code.jquery.com/jquery-1.10.2.js"></script>
  <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">

<script type='text/javascript'>

function suggest() {

var trial = $('input[name=q]').val().split(" ");
var queryWords = "";

for(var i=0;i<trial.length-1;i++){

	queryWords+=trial[i]+" ";

}

var word= trial.pop();

var xyzdata= {'testingcomplete':word};
$.ajax({ url: 'autocomplete.php',
         data: xyzdata,
         type: 'get',
	 datatype: 'JSON',})
     .done(function(data) {
var obj = jQuery.parseJSON(data);

str = JSON.stringify(obj);

var trial1=$('input[name=q]').val();
var myterms= new Array();
var length=JSON.stringify(obj['suggest']['suggest'][word]['numFound']);

var stopwords = new Array("a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also","although","always","am","among", "amongst", "amoungst", "amount", "an", "and", "another", "any","anyhow","anyone","anything","anyway", "anywhere", "are", "around", "as", "at", "back","be","became", "because","become","becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "bill", "both", "bottom","but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven","else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own","part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the");
for(i=0;i<parseInt(length,10);i++)
{


var stringW = JSON.stringify(obj['suggest']['suggest'][word]['suggestions'][i]['term']).replace(/['"']+/g, '');


for(var k=0;k<stopwords.length;k++){

var index = stringW.indexOf("of");


myterms[i] = queryWords + stringW;


}

}


$("#q").autocomplete({

	source :myterms

    });
});
}
//}




</script>

</head>
<body>
			<form accept-charset="utf-8" method="get">
				<label for="q">Search:</label>
				<input id="q" name="q" type="text" value="<?php echo htmlspecialchars($query,ENT_QUOTES,'utf-8');?>" onkeyup="suggest()";/>

				<input type="checkbox" name="algorithm" value="pgrank" <?php if($algo) echo htmlspecialchars("checked",ENT_QUOTES,'utf-8');?>>Page Rank<br>
				<input id="r" name="r" type="hidden" value="0"/>
	<p id='demo'> </p>

				<input type="submit"/>

			</form>


		<?php

		//	display	results
		if ($results)
		{
			$total	= (int)	$results->response->numFound;
			$start	= min(1,$total);
			$end	= min($limit,$total);
			?>
			<div>Results	<?php	echo $start;	?>	- <?php	echo $end;?>	of	<?php	echo $total;	?>:</div>
			<ol>
			<?php
			//	iterate	result	documents
			foreach ($results->response->docs as $doc)
			{
				?>
				<li>
				<?php
				$addr = substr($doc->id,strpos($doc->id, "http"));


$arr=array();
				$arr=csv_to_array();



				$link = str_replace("/home/dheemanth/Desktop/cinematic_final/newdir", "/Users/dheemanth/workspace/IR_assignment_3/newdir", $addr);


				?>
				<a href = "<?php echo htmlspecialchars($arr[$link]); ?>" ><h3><b><?php echo htmlspecialchars($arr[$link]); ?></b></h3></a>
				<div style = "margin-top :-20px;">
					<div>Title:<?php if($doc->title == "")echo htmlspecialchars("N/A"); echo htmlspecialchars($doc->title); ?> </div>
					<div>Date Created:<?php if($doc->date == "")echo htmlspecialchars("N/A"); echo htmlspecialchars($doc->date); ?> </div>
					<div>Size: <?php echo htmlspecialchars(((int)$doc->stream_size)/1000); ?> kB</div>
					<div>Author: <?php if($doc->meta_author == "")echo htmlspecialchars("N/A"); echo htmlspecialchars($doc->meta_author); ?> </div>
				</div>
				<?php
			//	iterate	document	fields	/	values
			?>
			</li>
			<br>
			<?php
		}
		?>
		</ol>
		<?php
		}
		?>
		</body>
</html>
