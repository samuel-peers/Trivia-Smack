function TasksViewModel(){
    var self = this;
    var loc = location.protocol + "//" + document.domain + ":" + location.port;
    var max = 5;
    var waitTime = 300;
    var red = "#c13636";
    var green = "#4dc136";
    var countDownTime = 10;
    var oneSecond = 1000;
    var theCountDown;
    var modal = document.getElementById("rulesModal");
    self.baseURI = "/api/question_data/"+max+"/";
    self.questionsURI = "";
    self.score = ko.observable(null);
    self.otherScore = ko.observable(null);
    self.questionCount = ko.observable(0);
    self.counter = ko.observable(countDownTime);
    self.isWaiting = ko.observable(false);
    self.showingCategories = ko.observable(false);
    self.onePlayerMode = ko.observable(true);
    self.isPlaying = ko.observable(false);
    self.questions = ko.observableArray();
    self.categories = ko.observableArray(["all", "geography",
    "history", "math and science", "pop culture", "other"]);

    self.counter.subscribe(function(newValue) {
        if (newValue === 0){
            startCounter();
            self.questionCount(self.questionCount()+1);
        }
    });

    self.questionCount.subscribe(function(newValue) {
        if (newValue == max){
            console.log("the game ended :)");
            endGame();
        }
    });

    self.startTwoPlayer = function() {
        socket = io.connect(loc, {"force new connection": true});
        self.questionsURI = self.baseURI+"all";
        self.onePlayerMode(false);
        self.isWaiting(true);

        socket.emit("join_game");

        socket.on("other_player_done", function(data){
            var result;

            if (data.msg === null)
                result = "Other player disconnected! You win!";
            else
                result = data.msg;

            self.otherScore(result);
        });

        socket.on("other_player_ready", function() {
            self.isWaiting(false);
            self.otherScore("Waiting for other player");
            self.initGame();
        });

        socket.on("clean_up", function() {
            socket.disconnect();
        });
    };

    self.pickCategory = function() {
        opposite = !self.showingCategories();
        self.showingCategories(opposite);
    };

    self.startOnePlayer = function(category) {
        self.onePlayerMode(true);
        self.isWaiting(false);
        self.otherScore(null);
        self.questionsURI = self.baseURI+category;
        self.showingCategories(false);
        self.initGame();
    };

    self.initGame = function() {
        fetchQuestions();
    };

    self.startGame = function() {
        self.questionCount(0);
        self.isPlaying(true);
        self.score(0);
        startCounter();
    };

    function endGame() {
        self.isPlaying(false);
        if (!self.onePlayerMode())
            socket.emit("game_over", {"score":self.score()});
    }

    self.processAnswer = function(optionObj) {

        if (optionObj.isCorrect){
            self.score(self.score()+ self.counter());
            optionObj.option("Right!");
            optionObj.bgColor(green);
        }
        else{
            optionObj.option("Wrong!");
            optionObj.bgColor(red);
        }

        setTimeout(function(){
            self.questionCount(self.questionCount()+1);
        }, waitTime);

        startCounter();
    };

    function startCounter(){
        clearInterval(theCountDown);
        self.counter(countDownTime);
        theCountDown =
        setInterval(function(){ self.counter(self.counter()-1); }, oneSecond);
    }

    self.ajax = function(uri, method, data) {
        var request = {
            url: uri,
            type: method,
            contentType: "application/json",
            accepts: "application/json",
            cache: false,
            dataType: "json",
            data: JSON.stringify(data),
            error: function(jqXHR) {
                console.log("Ajax failure");
            }
        };
        return $.ajax(request);
    };

    function fetchQuestions() {
        self.ajax(self.questionsURI, "GET").done(function(data) {
            self.buildQuestions(data);
            self.startGame();
        }).fail(function(jqXHR) {
            console.log("Ajax failure");
        });
    }

    self.buildQuestions = function(data) {
        self.questions.removeAll();

        for (var i = 0; i < data.questions.length; i++) {

            var obs_options = ko.observableArray();

            for (var j = 0; j < data.questions[i].options.length; j++){
                obs_options.push({
                    option: ko.observable(data.questions[i].options[j]),
                    bgColor: ko.observable(),
                    isCorrect: data.questions[i].answer == j
                });
            }

            self.questions.push({
                question: ko.observable(data.questions[i].question),
                options: obs_options,
                answer: data.questions[i].answer
            });
        }

        return self.questions();
    };

    self.showMenu = function() {
        socket.disconnect();
        self.isWaiting(false);
    };

    window.onclick = function(event) {
        if(event.target == modal)
            modal.style.display = "none";
    };

    self.closeRules = function() {
        modal.style.display = "none";
    };

    self.openRules = function() {
        modal.style.display = "block";
    };
}
