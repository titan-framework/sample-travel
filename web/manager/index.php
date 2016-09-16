<?php
$file = 'configure/titan.xml';

if (!file_exists ($file))
	die ('Arquivo de configuração <b>[configure/titan.xml]</b> não encontrado.');

$xml = file_get_contents ($file);

$regTag = '/<core-path>(.*?)<\/core-path>/s';

preg_match_all ($regTag, $xml, $match);

if (!isset ($match [1][0]))
{
	$regTag = '/core-path="(.*?)"/s';

	preg_match_all ($regTag, $xml, $match);
	
	if (!isset ($match [1][0]))
		die ('A diretiva <b>&lt;core-path&gt;&lt;/core-path&gt;</b> deve estar devidamente setada no arquivo de configuração <b>[configure/titan.xml]</b>.');
}

$corePath = $match [1][0];

if (!file_exists ($corePath .'switch.php'))
	die ('O core do Titan não foi localizado no caminho especificado em <b>&lt;core-path&gt;'. $corePath .'&lt;/core-path&gt;</b> no arquivo de configuração <b>[configure/titan.xml]</b>.');

try
{
	if (!isset ($corePath) || !file_exists ($corePath))
		throw new Exception ('Titan Core path on [titan.xml] is invalid!');
	
	require_once $corePath .'function/general.php';
	
	require_once $corePath .'function/legacy.php';
	
	require_once $corePath .'class/Xml.php';
	
	require_once $corePath .'class/Instance.php';
	
	require_once $corePath .'extra/Browscap.php';
	
	if (!@set_include_path ($corePath .'extra'))
		toLog ('Impossible to set include path. This cause Zend Framework load fail!');
	
	include_once 'Zend/Search/Lucene.php';
	
	include_once 'Zend/Search/Lucene/Exception.php';
	
	include_once 'Zend/Uri/Http.php';
	
	include_once 'Zend/Http/CookieJar.php';
	
	include_once 'Zend/Http/Client/Adapter/Socket.php';
	
	include_once 'Zend/Service/Twitter.php';
	
	$blockAccess = array (Instance::singleton ()->getDocPath (), Archive::singleton ()->getDataPath ());
	foreach ($blockAccess as $trash => $path)
		if (!file_exists ($path .'.htaccess') && is_writable ($path))
			if (!@copy (Instance::singleton ()->getCorePath () .'extra/access/.htaccess', $path .'.htaccess'))
				toLog ('Impossible to copy ['. Instance::singleton ()->getCorePath () .'extra/.htaccess] to ['. $path .']');
	
	$instance = Instance::singleton ();
	
	require_once $instance->getCorePath () .'class/AjaxLogon.php';
	
	session_name ($instance->getSession ());
	
	session_start ();
	
	$skin = Skin::singleton ();
	
	if (!Social::isActive ())
		die ('Integration with social networks is note enable! Please, contact administrator.');
	
	if (isset ($_GET['error']) && $_GET['error'] == 'access_denied')
		$_GET['error'] = __ ('Apparently you deny this application to access your profile data. Without granting this permission is not possible to authenticate!');
	
	$socialButtons = array ();
	
	while ($driver = Social::singleton ()->getSocialNetwork ())
	{
		if ($driver->authenticate ())
			try
			{
				if ($driver->login ())
				{
					?>
					<html><body onload="document.location='titan.php';"></body></html>
					<?
					exit ();
				}
			}
			catch (Exception $e)
			{
				$_GET ['error'] .= $e->getMessage ();
			}
			catch (PDOException $e)
			{
				$_GET ['error'] .= $e->getMessage ();
			}
		
		$socialButtons [$driver->getName ()] = array ($driver->getLoginUrl (), 'titan.php?target=loadFile&amp;file=repos/social/'. $driver->getName () .'/_resource/button.png');
	}
	?>
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
	<html xmlns="http://www.w3.org/1999/xhtml">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
			<title> <?= $instance->getName () ?> </title>
			<meta name="description" content="<?= $instance->getDescription () ?>" />
			
			<link rel="icon" href="<?= $skin->getIcon () ?>" type="image/ico" />
			<link rel="shortcut icon" href="<?= $skin->getIcon () ?>" type="image/ico" />
			
			<link rel="stylesheet" type="text/css" href="index.css" />
			<link href='http://fonts.googleapis.com/css?family=Lobster+Two' rel='stylesheet' type='text/css'>
			
			<script language="javascript" type="text/javascript">
			function setClientTimeZone ()
			{
				if ('<?= @$_COOKIE['_TITAN_TIMEZONE_'] ?>'.length)
					return false;
				
				xmlHttp = new XMLHttpRequest ();
				xmlHttp.open ('GET', 'titan.php?target=setClientTimeZone&z=' + getTimeZone (), true);
				xmlHttp.send (null);
			}
			</script>
		</head>
		<body onLoad="JavaScript: setClientTimeZone ();">
			<div id="main">
				<h1><img src="image/login.png" /></h1>
				<div class="info">
					<?= __ ('Use your favorite social network to access:') ?>
				</div>
				<div class="buttons">
					<?
					foreach ($socialButtons as $name => $array)
						echo '<img src="'. $array [1] .'" onclick="JavaScript: document.location=\''. $array [0] .'\';" border="0" />'
					?>
				</div>
				<div class="info">
					<?= __ ('Get our mobile app:') ?>
				</div>
				<div class="buttons">
					<?
					$mobileButtons = Instance::singleton ()->getMobile ();
					
					if (isset ($mobileButtons ['android']))
						echo '<img src="titan.php?target=loadFile&amp;file=interface/image/google-play.png" onclick="JavaScript: document.location=\''. $mobileButtons ['android'] .'\';" border="0" />'
					?>
				</div>
			</div>
			<div id="partners">
				<a href="http://www.facom.ufms.br/" target="_blank"><img src="image/facom.png" alt="Faculdade de Computação" /></a>
				<a href="http://www.ufms.br/" target="_blank"><img src="image/ufms.png" alt="Universidade Federal de Mato Grosso do Sul" /></a>
				<a href="http://www.embrapa.br/gado-de-corte/" target="_blank"><img src="image/embrapa.png" alt="Embrapa Gado de Corte" /></a>
			</div>
		</body>
	</html>
	<?
}
catch (PDOException $e)
{
	header ('HTTP/1.1 500 Internal Server Error');
	
	echo $e->getMessage ();
}
catch (Exception $e)
{
	header ('HTTP/1.1 500 Internal Server Error');
	
	echo $e->getMessage ();
}
?>