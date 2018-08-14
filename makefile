reset_database:
	sudo sudo -u postgres dropdb gtr
	sudo puppet agent --test
