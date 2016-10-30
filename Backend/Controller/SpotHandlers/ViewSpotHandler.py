import tornado.gen
from Controller.SpotHandlers.AbstractSpotHandler import AbstractSpotHandler
import json

class ViewSpotHandler(AbstractSpotHandler):
    @tornado.web.authenticated
    @tornado.gen.coroutine
    def get(self):
        spotID = self.get_argument("spotID")
        #needs to be finished