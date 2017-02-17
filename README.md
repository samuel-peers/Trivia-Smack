# Trivia Smack
A fast-paced 2 player trivia game.

As of right now:
A Flask-MongoDB stack running on a custom AWS EB instance.
Click start to have 3 random questions displayed. If the button flashes green, you answered correctly. If red, you answered wrong!

Play here: http://flask-environment.kxsucgnnpx.us-west-2.elasticbeanstalk.com/

Uses AWS Codepipeline for Continuous Integration. Code changes trigger deployment to our production server in AWS.

Requirements: eb CLI (`pip install awsebcli`)

To run:
- `clone https://github.com/samuel-peers/Trivia-Game.git`
- `cd Trivia-Game`
- `eb init -p python2.7 trivia_app`
- `eb create trivia_env`
