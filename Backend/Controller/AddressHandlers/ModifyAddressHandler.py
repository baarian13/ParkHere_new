import tornado.web
import tornado.gen
from Controller.AddressHandlers.AbstractAddressHandler import AbstractAddressHandler

SUCCESS = '200'
FAILURE = '401'
PARTIAL = '206'

class ModifyAddressHandler(AbstractAddressHandler):
    @tornado.web.authenticated # ensures that user has valid token/is signed in
    @tornado.gen.coroutine
    def get(self):
        pass

    #@tornado.web.authenticated # ensures that user has valid token/is signed in
    @tornado.gen.coroutine
    def post(self):
        '''
        -Each argument is specified in URL format.
        -If an argument is not specified (except picture), then the creation will fail.
        -writes 'success' if everything worked, writes 'partial' if only
            picture could not be added, writes 'failure' if user could not be created
        args:
            ownerEmail  -> str
            addressID   -> int
            spotType    -> int
            isCovered   -> bool
            description -> str
            picturePath -> B64 string (if not specified default is used)
        returns:
            success: 200
            failure: 401
            partial: 206
        '''
        result = SUCCESS
        ownerEmail = self.get_argument("ownerEmail","")
        address = self.get_argument("addressID","")
        spotType = self.get_argument("spotType","")
        isCovered = self.get_argument("isCovered","")
        description = self.get_argument("description","")
        picturePath = self.get_argument("picturePath", "")
        if spotType:
            try: self.db.updateSpotType(addressID, spotType)
            except: result = PARTIAL
        if isCovered:
            try: self.db.updateIsCovered(addressID, spotType)
            except: result = PARTIAL
        if description:
            try: self.db.updateDescription(addressID, description)
            except: result = PARTIAL
        if picturePath:
            try: self.db.updatePicturePath(addressID, picturePath)
            except: result = PARTIAL
        self.write(result)
