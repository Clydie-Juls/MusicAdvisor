# MusicAdvisor

Description: A simple program that suggests music to the user using Spotify API.

commands:
 Program state commands:
 -> auth - authenticates user. Allows access on program state commands that needs authentication.
 -> new (auth needed, paginated) - prompts new releases.
 -> featured (auth needed, paginated) - prompts featured playlists.
 -> categories (auth needed, paginated) - prompts category list.
 -> playlists <category-name> (auth needed, paginated) - prints playlists of a given category.

Pagination commands:
 -> next - moves to the next page.
 -> prev - moves to the previous page. 
