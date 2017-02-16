"""
routes.py
"""
from flask import render_template, request, session, redirect, jsonify
from web_app.business.AccessQuestions import AccessQuestions

from . import main

MAX_QUESTIONS = 3

@main.route("/")
def home_page():
    """The homepage"""
    access_questions = AccessQuestions()

    score = session["score"] if "score" in session.keys() else None

    session.clear()
    session["questions"] = access_questions.get_random_questions(MAX_QUESTIONS)
    session["question_count"] = 0
    session["score"] = 0

    if session["questions"] == []:
        raise Exception("No questions found!")

    return render_template("homePage.html", score=score)

@main.route("/question_page", methods=["GET", "POST"])
def question_page():
    """The questions page"""
    if request.method == "POST":
        session["score"] += 1

    q_count = session["question_count"]

    if q_count >= MAX_QUESTIONS:
        return redirect("/")

    question_obj = session["questions"][q_count]

    session["question_count"] = q_count + 1

    question = question_obj.question
    options = question_obj.options
    answer = options[question_obj.answer]

    return render_template("questionPage.html",\
       question=question,\
       options=options,\
       answer=answer)

@main.route("/api/android/question_data")
def question_data():
    """Returns questions to an android app client"""
    access_questions = AccessQuestions()
    questions = access_questions.get_random_questions(MAX_QUESTIONS)
    return jsonify(result=questions)
