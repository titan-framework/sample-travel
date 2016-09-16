<div style="padding: 10px; font: 12px Georgia, 'Times New Roman', Times, serif; line-height: 1.5;">
<?
foreach ($alerts as $key => $aux)
{
	$random = str_pad (rand (1, 9999), 4, '0', STR_PAD_LEFT);
	
	Alert::add ('_TEST_'. $aux [0] .'_', 'TEST'. $random, $users, array ('[MESSAGE]' => $aux [1]));
	
	echo 'Adicionado alerta '. $random .'! <br />';
}
?>
</div>