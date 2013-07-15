define [
	"cs!models/User"
], (User)-> 
  describe "User Model", ->
    user = null
    
    beforeEach ->
      user = new User
  
    it "Deaults should be set correctly", ->
      expect(user.get "id").toBe 1
      
    it "Default should be not logged in", ->
      expect(user.isLoggedIn()).not.toBeTruthy
      
#    it "Non Default user ID should be logged in", ->
#      user.id = 2
#      expected(user.isLoggedIn()).toBeTruthy