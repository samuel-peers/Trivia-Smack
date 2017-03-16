function TasksViewModel() {
    var socket = io.connect(location.protocol + "//" + document.domain + ":" + location.port);
    var self = this;
    var max = 3;
    var waitTime = 300;
    var grey = "#5A5A5A";
    var red = "#c13636";
    var green = "#4dc136";
    var white = "white";
    var countDownTime = 10;
    var oneSecond = 1000;
    var theCountDown;
    self.questionsURI = "/api/question_data/"+max;
    self.score = ko.observable(0);
    self.gameStarted = ko.observable(false);
    self.questionCount = ko.observable(0);
    self.startedOnce = ko.observable(false);
    self.counter = ko.observable(countDownTime);
    self.isWaiting = ko.observable(false);
    self.otherScore = ko.observable(0);
    self.showOtherScore = ko.observable(false);
    self.onePlayerMode = ko.observable(true);

    self.counter.subscribe(function(newValue) {
        if (newValue == 0){
            startCounter();
            self.questionCount(self.questionCount()+1);
        }
    });

    self.questionCount.subscribe(function(newValue) {
        if (newValue == max){

            if (!self.onePlayerMode())
                socket.emit("game_over", {"score":self.score()});

            self.questionCount(0);
            self.gameStarted(false);
        }
    });

    self.startWaiting = function() {
        self.isWaiting(true);
        socket.emit("join_game");
    }

    socket.on("other_player_done", function(data){
        var result;
        self.showOtherScore(true);

        if (data.msg == null)
            result = "Other player disconnected! You win!"
        else
            result = data.msg

        self.otherScore(result);
    });

    socket.on("other_player_ready", function() {
        self.onePlayerMode(false);
        self.showOtherScore(false);
        self.isWaiting(false);
        startGame();
    });

    self.startOnePlayer = function() {
        self.onePlayerMode(true);
        startGame();
    }

    function startGame() {
        self.startedOnce(true);
        self.score(0);
        self.questions = ko.observableArray();
        fetchQuestions();
        self.gameStarted(true);
        startCounter();
    }

    self.processAnswer = function(optionObj) {

        if (optionObj.isCorrect){
            self.score(self.score()+1)
            optionObj.option("Right!");    
            optionObj.bgColor(green);
            optionObj.textColor(white);
        }
        else{
            optionObj.option("Wrong!");
            optionObj.bgColor(red);
            optionObj.textColor(white);
        }

        setTimeout(function(){ 
            self.questionCount(self.questionCount()+1); 
        }, waitTime);

        startCounter();
    }

    function startCounter(){
        clearInterval(theCountDown);
        self.counter(countDownTime);
        theCountDown = 
        setInterval(function(){ self.counter(self.counter()-1) }, oneSecond);
    }

    self.ajax = function(uri, method, data) {
        var request = {
            url: uri,
            type: method,
            contentType: "application/json",
            accepts: "application/json",
            cache: false,
            dataType: 'json',
            data: JSON.stringify(data),
            error: function(jqXHR) {
                console.log("ajax error " + jqXHR.status);
            }
        };
        return $.ajax(request);
    }

    function fetchQuestions(){
        self.ajax(self.questionsURI, "GET").done(function(data) {
            for (var i = 0; i < data.questions.length; i++) {

                var obs_options = ko.observableArray();

                for (var j = 0; j < data.questions[i].options.length; j++){
                    obs_options.push({
                        option: ko.observable(data.questions[i].options[j]),
                        bgColor: ko.observable(white),
                        textColor: ko.observable(grey),
                        isCorrect: data.questions[i].answer == j
                    });
                }

                self.questions.push({
                    question: ko.observable(data.questions[i].question),
                    options: obs_options,
                    answer: data.questions[i].answer
                });
            }
        });
    }
}
ko.applyBindings(new TasksViewModel(), $("#main")[0]);