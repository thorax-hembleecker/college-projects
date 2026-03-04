Project 1

For this project, I designed a wiki supporting user-created pages, with important user-specific links on the top navigation bar, and further links on the sidebar.

Important pages:
- Top navigation bar
	- Create a page! (page-editor.html): Pulls up a form that allows you to create a new page. If clicked when not logged in, takes you to the login screen.
	- Sign up (newuser.html): Allows the user to fill out a form to create an account. Automatically logs you in once submitted, and is replaced with a link to the user's personal page when logged in.
	- Log in (login.html): Allows the user to fill out a form to log in to an existing account, and is replaced with "Log out" once logged in.
	- User page (/users/<username>.html): The page for an individual user. Appears on the navbar when logged in, showing the user's display name with a link to their page. Replaced by "Sign up" when not logged in.
- Sidebar
	- Home (index.html): The home page of The Grand Wiki of Whatever You Want. Features basic information about the site.
	- All pages (pages.html): A list of all pages on the site.
	- Contributors (users.html): A list of all users on the site.
- Other:
	- Wiki page (/wiki/<url>.html): A user-created wiki page. Accessible via the "All pages" page, or via the page of the user who created it.



There are 2 database tables in the wiki: User and Page. They are configured with the following attributes:
- User:
	- id: The user's unique numerical ID, and the primary key to the table.
	- username: The user's unique username, used for login.
	- displayname: The user's unique display name, shown to other users.
	- password_hash: The user's hashed password.
	- bio: The user's self-written biography, shown on their user page.
	- pages: The list of pages created by the user (a relationship referencing Page).
- Page:
	- url: The page's unique URL, and the primary key to the table.
	- title: The title of the page.
	- body: The text of the page.
	- references: Sources for the page, if given.
	- creator_id: The username of the user who created the page, and a foreign key to User.username.
	- creator: See above (but as a relationship referencing User).