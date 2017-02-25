"""
DataAccessStub.py
"""
import random
from web_app.persistence.DataAccessInterface import DataAccessInterface
from web_app.objects.Question import Question

class DataAccessStub(DataAccessInterface):
    """Stub database"""

    def __init__(self, name):
        self.db_name = name
        self.questions = []

    def open(self):
        """Open the database"""
        print "Opened database"
        self.questions.append(Question(0, "How much does a male "
                                       "Polar Bear weigh?",
                                       ["1200 lbs", "1000 lbs", "600 lbs",
                                        "Enough to break the ice"], 1))
        self.questions.append(Question(1, "Is the square root of 10:",
                                       ["zero", "greater than 3",
                                        "less than 3"], 1))
        self.questions.append(Question(2, "Platypuses lay eggs",
                                       ["true", "false"], 0))
        self.questions.append(Question(3, "Helsinki is the capitol of:",
                                       ["Sweden", "Russia", "Finland",
                                        "Iceland"], 2))
        self.questions.append(Question(4, "If x+y=3 and 2x+y=4, then x equals",
                                       ["0", "1", "4", "3"], 1))
        self.questions.append(Question(5, "If x+y<11 and x>6, then y is:",
                                       ["positive", "negative",
                                        "Not determinable"], 2))
        self.questions.append(Question(6, "The plural of bison is:",
                                       ["bisons", "buffalo", "bison",
                                        "buffalos"], 2))
        self.questions.append(Question(7, "21, 25, 33, 49, 81, ",
                                       ["162", "113", "144", "145"], 2))
        self.questions.append(Question(8, "The Balkans are in:",
                                       ["South America", "Europe", "Australia",
                                        "Asia"], 1))

    def close(self):
        """Close the database"""
        print "Closed database"
        self.questions = None

    def get_question(self, _id):
        """
        Grab a question by its id
        Note: this should only be returning one question. Should add
        checks for that.
        """
        return next((x for x in self.questions if x._id == _id), None)

    def get_random_question(self):
        """Grab a random question from the DB"""
        num_qs = self.get_num_questions()
        rq_num = random.randint(0, num_qs-1) if num_qs > 0 else 0
        return self.questions[rq_num]

    def get_all_questions(self):
        """Return a list of all the questions"""
        return self.questions

    def get_num_questions(self):
        """Return the number of questions"""
        return len(self.questions)

    def insert_question(self, _id, question, options, answer):
        # Will make these so id's are generated, not given as a parameter
        self.questions.append(Question(_id, question, options, answer))

    def update_question(self, _id, question=None,
                        options=None, answer=None):
        """
        Takes a question id and updates its corresponding question.
        """
        question_obj = self.get_question(_id)

        if question is not None:
            question_obj.question = question
        if options is not None:
            question_obj.options = options
        if answer is not None:
            question_obj.answer = answer

    def delete_question(self, _id):
        for question in self.questions:
            if question._id == _id:
                self.questions.remove(question)
