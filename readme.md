
# Recipe Management System - Backend

This is the backend service of the Recipe Management System, developed using **Spring Boot**. It provides RESTful APIs for managing user authentication, recipes, meal plans, shopping lists, and personalized recommendations. This service interacts with a MySQL database (via AWS RDS) and uses Redis for caching (via AWS ElastiCache).

## Table of Contents

- [Project Description](#project-description)
- [Technologies Used](#technologies-used)
- [Installation and Running](#installation-and-running)
- [API Usage](#api-usage)
- [Credits](#credits)
- [License](#license)

## Project Description

The Recipe Management Backend is the core of a full-stack application designed to help users manage their recipes, plan meals, and generate shopping lists. It also offers personalized recipe recommendations based on user preferences.

Key functionalities include:

- **User Authentication**: Secure login and registration via JWT.
- **Recipe Management**: CRUD operations for users to create, edit, delete, and retrieve recipes.
- **Meal Planning**: Plan meals by adding recipes to specific dates.
- **Shopping Lists**: Automatically generate a shopping list based on the ingredients required for planned meals.
- **Recommendation System**: A collaborative filtering algorithm suggests recipes and meal plans based on user history and preferences.

## Technologies Used

- **Java 21**: Backend logic and service implementation.
- **Spring Boot**: Framework for building scalable and maintainable backend services.
- **Spring Security**: Handles authentication and authorization using JWT.
- **MySQL (AWS RDS)**: Relational database for persistent data storage.
- **Redis (AWS ElastiCache)**: For caching frequently accessed data to improve performance.
- **AWS Lambda**: Serverless architecture for hosting the backend.
- **Maven**: For project management and dependency resolution.

## Installation and Running

### Prerequisites

- Java 21
- Maven
- MySQL Database (AWS RDS)
- Redis (AWS ElastiCache)

### Step-by-Step Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Simple-Recipes/backend.git
   cd backend
   ```

2. **Configure database settings**:  
   Update the following configurations in `application.properties` with your RDS and Redis credentials:

   ```properties
   recipes:
     datasource:
       driver-class-name: com.mysql.cj.jdbc.Driver
       host: recipes-rds-1.cluq6g4cqy61.eu-west-1.rds.amazonaws.com
       url: jdbc:mysql://recipes-rds-1.cluq6g4cqy61.eu-west-1.rds.amazonaws.com:3306/simulaterecipes
       port: 3306
       database: simulaterecipes
       username: admin
       password: Password1234
     redis:
       host: recipes-redis-s1a59u.serverless.euw1.cache.amazonaws.com:6379
       port: 6379
       # password: <your-redis-password>
   ```

3. **Build and Run**:
   Use Maven to build and run the project.
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access the API**:  
   The backend will be available at `http://localhost:8080`.

## API Usage

Below are the main API endpoints provided by the backend service. The `baseUrl` for all API requests is:

```
https://o5m78y4yqa.execute-api.eu-west-1.amazonaws.com/recipes/
```

### Authentication

- **POST** `/user/register`: Register a new user.
- **POST** `/user/sendCode`: Send a verification code to the user.
- **POST** `/user/loginWithCode`: Login with a verification code.
- **POST** `/user/loginWithPassword`: Login with username and password.
- **GET** `/user/profile`: Get the user's profile information.
- **PUT** `/user/profile`: Update the user's profile.
- **POST** `/user/forgotPassword`: Initiate the forgot password process.

### Recipes

- **GET** `/recipes/{id}`: Get the details of a specific recipe.
- **GET** `/recipes/popular`: Get a list of popular recipes.
- **GET** `/recipes/tag/popular?tag={tag}&page={page}&pageSize={pageSize}`: Get popular recipes by tag.
- **GET** `/recipes/search?keyword={query}`: Search for recipes by keyword.
- **POST** `/recipes/publish`: Publish a new recipe.
- **GET** `/recipes/getAllMyRecipes`: Get all recipes created by the current user.
- **DELETE** `/recipes/delete`: Delete a specific recipe.
- **GET** `/recipes/edit/{id}`: Get recipe details for editing.
- **PUT** `/recipes/edit`: Edit an existing recipe.
- **GET** `/recipes/all`: Get all recipes.

### Comments

- **POST** `/comments/postRecipeComment`: Post a comment on a recipe.
- **GET** `/comments/getRecipeComments`: Get all comments for a specific recipe.
- **GET** `/comments/getAllMyComments`: Get all comments posted by the current user.
- **DELETE** `/comments/deleteComment`: Delete a specific comment.

### Favorites

- **POST** `/favorites/add`: Add a recipe to favorites.
- **DELETE** `/favorites/remove`: Remove a recipe from favorites.
- **GET** `/favorites/getAllMyFavorites`: Get all favorite recipes for the current user.

### Likes

- **POST** `/likes/likeRecipes`: Like a specific recipe.
- **DELETE** `/likes/unlikeRecipe`: Unlike a specific recipe.
- **GET** `/likes/getAllMyLikes`: Get all recipes liked by the current user.
- **GET** `/likes/count`: Get the like count for a specific recipe.

### Recommendations

- **POST** `/recommendation`: Get personalized recipe recommendations.

### Tags

- **GET** `/tags/getAllMyTags`: Get all tags created by the current user.
- **POST** `/tags/addNewTag`: Add a new tag.
- **DELETE** `/tags/{id}`: Delete a specific tag.

### Inventory

- **GET** `/inventory/getAllMyInventory`: Get all items in the user's inventory.
- **POST** `/inventory/add`: Add a new item to the inventory.
- **DELETE** `/inventory/delete`: Delete an item from the inventory.
- **PUT** `/inventory/edit`: Edit an item in the inventory.

For more detailed API documentation, refer to the [API Docs](https://o5m78y4yqa.execute-api.eu-west-1.amazonaws.com/recipes/doc.html).

## Credits

This project was developed by the following contributors:

### **Contributors and Responsibilities**

| **Contribution Area** | **Contributor**          |
|-----------------------|--------------------------|
| Framework (Backend structure & multi-module design) | xiaoha                   |
| API Development (Shared work across the project) | xiaoha & raining          |
| Database Design & Implementation | xiaoha                   |
| Algorithm Development (Recommendation system) | xiaoha                   |
| AWS Deployment (RDS, ElastiCache, Lambda) | xiaoha                   |
| Controller (Inventory & ShoppingList) | raining                  |
| Other Controllers | xiaoha                   |

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.


