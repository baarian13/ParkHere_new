import tornado.gen
from Controller.SpotHandlers.AbstractSpotHandler import AbstractSpotHandler
import json


class ViewPostingsHandler(AbstractSpotHandler):
    @tornado.web.authenticated
    @tornado.gen.coroutine
    def get(self):
        ownerEmail = self.get_argument('email')
        if ownerEmail:
            results = self.db.getSpotIDsOwnedBy(ownerEmail)
            self.write(json.dumps(results))
