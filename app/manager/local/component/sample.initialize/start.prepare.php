<?php

$users = Database::singleton ()->query ("SELECT _id FROM _user")->fetchAll (PDO::FETCH_COLUMN);

$alerts = array (
	array ('W', 'Morbi semper purus eu nisi venenatis, quis ornare elit imperdiet.'),
	array ('I', 'Duis nec sem eget enim fringilla pretium in at odio.'),
	array ('C', 'Morbi vel risus eu metus porttitor dictum.'),
	array ('S', 'Suspendisse eu eros id tellus convallis placerat et at elit.'),
	array ('W', 'Donec ac dui in mi vestibulum imperdiet.'),
	array ('I', 'Proin vel purus quis leo adipiscing dictum nec vitae ipsum.'),
	array ('C', 'Nunc pharetra augue vel massa fringilla vulputate.'),
	array ('S', 'Vivamus eu erat viverra, consequat diam in, adipiscing turpis.'),
	array ('W', 'Integer convallis mi sed purus tincidunt bibendum.'),
	array ('I', 'Sed a arcu feugiat, pharetra neque ac, placerat nulla.'),
	array ('C', 'Aliquam faucibus tortor et est sollicitudin ullamcorper.'),
	array ('S', 'Quisque eu ante nec libero consequat facilisis rutrum dapibus tortor.')
);