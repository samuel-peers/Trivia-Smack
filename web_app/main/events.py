"""
events.py
"""
from flask import request
from flask_socketio import emit, join_room
from web_app.business.ConnectionController import ConnectionController
from .. import socketio

cc = ConnectionController()

@socketio.on("join_game")
def join_game():
    print "request.sid=%s" % request.sid
    join_room(request.sid)
    cc.join_waiting(request.sid)
    
    emit("join_waiting")

    emit("join_waiting")

    if cc.game_ready():
        cc.join_playing()
        emit("other_player_ready", room=request.sid)
        emit("other_player_ready", room=cc.get_partner(request.sid))

@socketio.on("game_over")
def game_over(message):
    print "game over!"
    if cc.get_partner(request.sid) is not None:

        emit("other_player_done", {"msg":message["score"]},\
            room=cc.get_partner(request.sid))

        cc.leave_playing(request.sid)

@socketio.on("disconnect")
def disconnect():
    other_id = cc.get_partner(request.sid)

    if other_id is not None:
        emit("other_player_done", {"msg":None}, room=other_id)

    cc.leave_playing(request.sid)
    cc.leave_waiting(request.sid)
