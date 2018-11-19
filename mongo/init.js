use admin;
db.createRole({role:'sysadmin',roles:[],privileges:[{resource:{anyResource:true},actions:['anyAction']}]});
db.createUser({user:'admin',pwd:'FM3.1415926',roles:[{role:'sysadmin',db:'admin'}]});

use monitor;
db.createUser({user:'monitor',pwd:'futuremove',roles:[{role:'readWrite',db:'monitor'}]});

db.createUser({user:'dspt',pwd:'futuremove',roles:[{role:'readWrite',db:'dspt'}]});
