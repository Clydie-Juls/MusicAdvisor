# MusicAdvisor

**Description:** A simple program that suggests music to the user using Spotify API.

## Commands

### Program State Commands

- `auth` - authenticates user. Allows access on program state commands that needs authentication.
- `new` (auth needed, paginated) - prompts new releases.
- `featured` (auth needed, paginated) - prompts featured playlists.
- `categories` (auth needed, paginated) - prompts category list.
- `playlists <category-name>` (auth needed, paginated) - prints playlists of a given category.

### Pagination Commands

- `next` - moves to the next page.
- `prev` - moves to the previous page.

**Note:** The `new`, `featured`, `categories`, and `playlists` commands require authentication to use. They are also paginated, meaning that they will display a limited number of items at a time and allow the user to navigate through pages using the `next` and `prev` commands.
