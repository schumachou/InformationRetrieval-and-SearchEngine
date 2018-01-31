<?php

// make sure browsers see this page as utf-8 encoded HTML
header('Content-Type: text/html; charset=utf-8');

$limit = 10;
$query = isset($_REQUEST['q']) ? $_REQUEST['q'] : false;

$sort = isset($_REQUEST['rank']) && $_REQUEST['rank'] == 'pagerank' ? array(
    'sort' => 'pageRankFile desc') : false;
$results = false;

if ($query)
{
  // The Apache Solr Client library should be on the include path
  // which is usually most easily accomplished by placing in the
  // same directory as this script ( . or current directory is a default
  // php include path entry in the php.ini)
  require_once('Apache/Solr/Service.php');

  // create a new solr service instance - host, port, and webapp
  // path (all defaults in this example)
  $solr = new Apache_Solr_Service('localhost', 8983, '/solr/myexample');

  // if magic quotes is enabled then stripslashes will be needed
  if (get_magic_quotes_gpc() == 1)
  {
    $query = stripslashes($query);
  }

  // in production code you'll always want to use a try /catch for any
  // possible exceptions emitted  by searching (i.e. connection
  // problems or a query parsing error)
  
  //provide additional parameters to the Solr server
  $additionalParameters = array(
    'fq' => 'a filtering query',
    'facet' => 'true',
    // notice I use an array for a multi-valued parameter 
    'facet.field' => array(
      'field_1',
      'field_2'
    ) 
  );
  
//  $sort = array(
//    'sort' => 'pageRankFile desc',  
//  );
  
  try
  {
//    $results = $solr->search($query, $start, $rows, $additionalParameters);
    if ($sort) 
      $results = $solr->search($query, 0, $limit, $sort);
    else
      $results = $solr->search($query, 0, $limit);
  }
  catch (Exception $e)
  {
    // in production you'd probably log or email this error to an admin
    // and then show a special message to the user but for this example
    // we're going to show the full exception
    die("<html><head><title>SEARCH EXCEPTION</title><body><pre>{$e->__toString()}</pre></body></html>");
  }
}

?>
<html>
  <head>
    <title>PHP Solr Client Example</title>
  </head>
  <body>
    <form  accept-charset="utf-8" method="get">
      <label for="q">Search:</label>
      <input id="q" name="q" type="text" value="<?php echo htmlspecialchars($query, ENT_QUOTES, 'utf-8'); ?>"/>
      <input type="radio" name="rank" value="tf-idf" <?php if(isset($_REQUEST['rank']) && $_REQUEST['rank']=="tf-idf") echo "checked";?>>tf-idf
      <input type="radio" name="rank" value="pagerank" <?php if (isset($_REQUEST['rank']) && $_REQUEST['rank']=="pagerank") echo "checked";?>>page rank
      <input type="submit"/>
    </form>
<?php

// display results
if ($results)
{
  
  //build a file-to-url dictionary
  $fileUrlMap = array();
  $file = fopen("WSJ_map.csv","r");
  while(!feof($file)){
    $line = fgetcsv($file);
    $fileUrlMap[$line[0]] = $line[1];
  }
  fclose($file);   
  
  $total = (int) $results->response->numFound;
  $start = min(1, $total);
  $end = min($limit, $total);
?>
    <div>Results <?php echo $start; ?> - <?php echo $end;?> of <?php echo $total; ?>:</div>
    <ol>
<?php
  // iterate result documents
  foreach ($results->response->docs as $doc)
  {
?>
      <li>
        <table style="border: 1px solid black; text-align: left">
<?php
    // iterate document fields / values
    $data = array();
    foreach ($doc as $field => $value)
    {
      if ($field == 'id' || $field == 'description' || $field == 'title' || $field == 'og_url')
      {
        $data[$field] = $value;
      }
    }
?>
          <tr>
            <th><?php echo htmlspecialchars('Title', ENT_NOQUOTES, 'utf-8'); ?></th>
            <td><?php 
                  if (array_key_exists('og_url', $data)) 
                    echo "<a href='".htmlspecialchars($data['og_url'], ENT_NOQUOTES, 'utf-8')."'>".htmlspecialchars($data['title'], ENT_NOQUOTES, 'utf-8')."</a>";
                  else 
                    echo "<a href='".$fileUrlMap[explode("/",$data['id'])[8]]."'>".htmlspecialchars($data['title'], ENT_NOQUOTES, 'utf-8')."</a>";
                ?>
            </td>
          </tr>
          <tr>
            <th><?php echo htmlspecialchars('URL', ENT_NOQUOTES, 'utf-8'); ?></th>
            <td><?php 
                  if (array_key_exists('og_url', $data)) 
                  {
                    echo "<a href='".htmlspecialchars($data['og_url'], ENT_NOQUOTES, 'utf-8')."'>".htmlspecialchars($data['og_url'], ENT_NOQUOTES, 'utf-8')."</a>";
                  }
                  else
                  {
                    $fileID = explode("/",$data['id'])[8];
                    echo "<a href='".$fileUrlMap[$fileID]."'>".$fileUrlMap[$fileID]."</a>";
                  }     
                ?>
            </td>
          </tr>
          <tr>
            <th><?php echo htmlspecialchars('ID', ENT_NOQUOTES, 'utf-8'); ?></th>
            <td><?php echo htmlspecialchars($data['id'], ENT_NOQUOTES, 'utf-8'); ?></td>
          </tr>
          <tr>
            <th><?php echo htmlspecialchars('Description', ENT_NOQUOTES, 'utf-8'); ?></th>
            <td><?php 
                  if (array_key_exists('description', $data)) 
                  {
                    echo htmlspecialchars($data['description'], ENT_NOQUOTES, 'utf-8');
                  }
                ?>
            </td>
          </tr>
        </table>
      </li>
<?php
  }
?>
    </ol>
<?php
}
?>
  </body>
</html>