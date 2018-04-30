<?php


            $url_detail = "http://localhost:8983/solr/cinematicfinal/suggest?q=".$term."%0A&wt=json&indent=true";
            $json_info = file_get_contents($url_detail);
			echo $json_info;
			$js = json_decode($json_info,true);

           	$term = $_GET['testingcomplete'];

            $url_detail = "http://localhost:8983/solr/cinematicfinal/suggest?q=".urlencode($term)."&wt=json&indent=true";

           $json_info = file_get_contents($url_detail);
			echo $json_info;
			$js = json_decode($json_info,true);

?>
