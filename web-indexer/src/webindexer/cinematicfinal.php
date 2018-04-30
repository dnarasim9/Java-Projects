<?php
//	make	sure	browsers	see	this	page	as	utf-8	encoded	HTML
header('Content-Type:	text/html;	charset=utf-8');
$limit	= 10;
$query	= isset($_REQUEST['q'])?$_REQUEST['q']:false;
$algo	= isset($_REQUEST['algorithm'])?$_REQUEST['algorithm']:false;
$results	= false;
if ($query)
{
	//	The	Apache	Solr	Client	library	should	be	on	the	include	path
	//	which	is	usually	most	easily	accomplished	by	placing	in	the
	//	same	directory	as	this	script	(	.	or	current	directory	is	a	default
	//	php	include	path	entry	in	the	php.ini)
	require_once('/home/dheemanth/solr-php-client/Apache/Solr/Service.php');
	//	create	a	new	solr	service	instance	- host,	port,	and	corename
	//	path	(all	defaults	in	this	example)
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
/**
* Example
*/

?>

<html>
		<head>
			<title>PHP Solr Client</title>
		</head>
		<body>
			<form accept-charset="utf-8" method="get">
				<label for="q">Search:</label>
				<input id="q" name="q" type="text" value="<?php echo htmlspecialchars($query,ENT_QUOTES,'utf-8');?>"/>
				<input type="checkbox" name="algorithm" value="pgrank" <?php if($algo) echo htmlspecialchars("checked",ENT_QUOTES,'utf-8');?>>Page Rank<br>
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

				//echo $addr;
				//print_r(csv_to_array());
$arr=array();
				$arr=csv_to_array();


				$link = str_replace("/home/dheemanth/Desktop/cinematic_final/newdir", "/Users/ashwini/workspace/IR_assignment_3/newdir", $addr);


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
