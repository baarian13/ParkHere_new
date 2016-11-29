import tornado.web
import tornado.gen
from Controller.SpotHandlers.AbstractSpotHandler import AbstractSpotHandler

SUCCESS = '200'
FAILURE = '401'
PARTIAL = '206'

class ModifyPriceHandler(AbstractSpotHandler):
    @tornado.web.authenticated # ensures that user has valid token/is signed in
    @tornado.gen.coroutine
    def get(self):
        pass

    #Must send address and owner email in order to update picture
    #@tornado.web.authenticated # ensures that user has valid token/is signed in
    @tornado.gen.coroutine
    def post(self):
        result = SUCCESS
        spotID = self.get_argument("spotID","")
        price = self.get_argument("price","")
        if spotID and price:
            try:
                self.db.updatePrice(spotID, price)
            except:
                result = FAILURE
        self.write(result)
