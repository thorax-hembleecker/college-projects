import unittest
from flask import current_app
from app import create_app, db

class BasicsTestCase(unittest.TestCase):
    def setUp(self): # Setup
        self.app = create_app("testing")
        self.app_context = self.app.app_context()
        self.app_context.push()
        db.create_all()
        self.client = self.app.test_client(use_cookies=True)
    
    def tearDown(self): # Teardown
        db.session.remove()
        db.drop_all()
        self.app_context.pop()
        
    def test_app_exists(self): # Tests that the app exists
        self.assertFalse(current_app is None)
        
    def test_app_is_testing(self): # Tests that the app is in test configuration
        self.assertTrue(current_app.config["TESTING"])
        
    def test_home(self): # Tests that the home page loads
        response = self.client.get("/")
        self.assertEqual(response.status_code, 200)

    def test_coolpage_loads(self): # Tests that the Cool Page loads
        import app.main.views
        app.main.views.createUser("glep", "mrfrog123")
        response = self.client.post("/login.html", data={
            "username" : "glep",
            "password" : "mrfrog123",
            "remember_me" : True
        })
        response = self.client.get("/")
        response = self.client.get("/coolpage", follow_redirects=True)
        self.assertEqual(response.status_code, 200)
    
    def test_add_user_loads(self): # Tests that the signup page loads
        response = self.client.get("/signup")
        self.assertEqual(response.status_code, 200)
    
    def test_add_user(self): # Tests that the signup form works
        response = self.client.post("/signup", data={
            "username" : "glep",
            "password" : "mrfrog123"
        })
        self.assertEqual(response.status_code, 302) # 302 means successful insert
        response = self.client.get("/")
        self.assertTrue("glep" in response.get_data(as_text=True))
            
    def test_404(self): # Tests that the 404 error page works
        response = self.client.get("/notapage.html")
        self.assertEqual(response.status_code, 404)
        self.assertTrue("page not found" in response.get_data(as_text=True))
        
    def test_login(self): # Tests that logging in works
        import app.main.views
        app.main.views.createUser("glep", "mrfrog123")
        response = self.client.post("/login.html", data={
            "username" : "glep",
            "password" : "mrfrog123",
            "remember_me" : True
        })
        self.assertEqual(response.status_code, 302)
        
        response = self.client.get("/coolpage")
        self.assertEqual(response.status_code, 200)

        response = self.client.get("/")
        self.assertEqual(response.status_code, 200)
        response = self.client.get("/logout")
        self.assertEqual(response.status_code, 302)
        
    def test_users(self): # Tests that the generated page for the user works
        import app.main.views
        app.main.views.createUser("glep", "mrfrog123")
        response = self.client.post("/login.html", data={
            "username" : "glep",
            "password" : "mrfrog123",
            "remember_me" : True
        })
        self.assertEqual(response.status_code, 302)
        response = self.client.get("/users/glep")
        self.assertEqual(response.status_code, 200)
        self.assertTrue("glep" in response.get_data(as_text=True))
    
    def test_login_incorrect(self): # Tests that an incorrect password does not log you in
        import app.main.views
        app.main.views.createUser("glep", "mrfrog123")
        response = self.client.post("/login.html", data={
            "username" : "glep",
            "password" : "mrfrog12345",
            "remember_me" : True
        })
        response = self.client.get("/login.html")
        self.assertTrue("log in" in response.get_data(as_text=True))
    
    def test_logout(self): # Tests that the logout page works
        import app.main.views
        app.main.views.createUser("glep", "mrfrog123")
        response = self.client.post("/login.html", data={
            "username" : "glep",
            "password" : "mrfrog123",
            "remember_me" : True
        })
        self.assertEqual(response.status_code, 302)
        response = self.client.get("/logout")
        response = self.client.get("/")
        self.assertTrue("Sign Up" in response.get_data(as_text=True))


if __name__ == "__main__":
    unittest.main()