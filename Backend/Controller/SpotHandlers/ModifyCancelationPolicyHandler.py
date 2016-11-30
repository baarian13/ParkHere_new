import tornado.web
import tornado.gen
from Controller.SpotHandlers.AbstractSpotHandler import AbstractSpotHandler

SUCCESS = '200'
FAILURE = '401'
PARTIAL = '206'

class ModifyCancelationPolicyHandler(AbstractSpotHandler):
    @tornado.web.authenticated # ensures that user has valid token/is signed in
    @tornado.gen.coroutine
    def get(self):
        pass

    @tornado.gen.coroutine
    def post(self):
        result = SUCCESS
        spotID = self.get_argument("spotID","")
        cPolicy = self.get_argument("cPolicy","")
        if spotID and cPolicy:
            try:
                self.db.modifyCancelationPolicy(spotID, cPolicy)
            except:
                result = FAILURE
        self.write(result)
