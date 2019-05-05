# AppSheet Programming Project

## Questions
- How do we handle multiple users with the same age if there isn't room for all of them in the top K list? (Please seem assumptions for how I currently handle this case)
- Should we propagate any errors or validations to the UI. For example, should be alert the user if the underlying services are down?
- How should phone number be validated? Do we care about local numbers (i.e., without area code / only 7 digits) or extentions?

## Assumptions
- If there are multiple users with the same age, the first ones we see will make it into the top K list and others will be ignored.
- We are logging all errors and not propagating them to the UI.
- I wasn't sure if adding unit tests was important for this project so I've left them out for now. I'm not really into TDD but I usually add unit test as they are helpful when refactoring.
- If any individual request fails and there are more user Ids available, I log the error and continue making requests until all user ids or valid user details are fetched.
- I'm currently allowing local phone numbers and extensions.

## How to automatically test app
- Use libraries like Mockito to create unit tests that mock various use cases of the service used by the app.
- Create integration tests to run in a test/QA environment where we can stage data so we know the expected behavior (i.e., result) of the backend service.

## How the design of the end-to-end service + app should change if the dataset were three orders of magnitude larger:
- Put the underlying service on multiple servers and use a load balancer to manage the routing of incoming requests.
- Add a method for getting the user details in bulk so that we don't have to make individual service calls for each user id. That is, given a list of user ids, the service would return a list of user details.
- Update the backend service I wrote to query the underlying services using multiple threads so that we can get the user details concurrently.
- In the UI, add some kind of "in progress" indicator so the user is aware if the service is taking a while to run to completion.

## Output of the 5 youngest users with valid phone numbers sorted by name: 

User{id=15, name='julia', age=24, number='(555) 555-5555', photo='https://appsheettest1.azurewebsites.net/female-5.jpg', bio='Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ex sapien, interdum sit amet tempor sit amet, pretium id neque. Nam ultricies ac felis ut lobortis. Praesent ac purus vitae est dignissim sollicitudin. Duis iaculis tristique euismod. Nulla tellus libero, gravida sit amet nisi vitae, ultrices venenatis turpis. Morbi ut dui nunc.'}

User{id=26, name='praveen', age=22, number='555-5555', photo='https://appsheettest1.azurewebsites.net/male-92.jpg', bio='Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ex sapien, interdum sit amet tempor sit amet, pretium id neque. Nam ultricies ac felis ut lobortis. Praesent ac purus vitae est dignissim sollicitudin. Duis iaculis tristique euismod. Nulla tellus libero, gravida sit amet nisi vitae, ultrices venenatis turpis. Morbi ut dui nunc.'}

User{id=19, name='richard', age=20, number='555-555-5555', photo='https://appsheettest1.azurewebsites.net/male-24.png', bio='Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ex sapien, interdum sit amet tempor sit amet, pretium id neque. Nam ultricies ac felis ut lobortis. Praesent ac purus vitae est dignissim sollicitudin. Duis iaculis tristique euismod. Nulla tellus libero, gravida sit amet nisi vitae, ultrices venenatis turpis. Morbi ut dui nunc.'}

User{id=18, name='robert', age=21, number='(555) 555-5555', photo='https://appsheettest1.azurewebsites.net/male-21.jpg', bio='Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ex sapien, interdum sit amet tempor sit amet, pretium id neque. Nam ultricies ac felis ut lobortis. Praesent ac purus vitae est dignissim sollicitudin. Duis iaculis tristique euismod. Nulla tellus libero, gravida sit amet nisi vitae, ultrices venenatis turpis. Morbi ut dui nunc.'}

User{id=14, name='santiago', age=25, number='(555)555-5555', photo='https://appsheettest1.azurewebsites.net/male-79.jpg', bio='Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ex sapien, interdum sit amet tempor sit amet, pretium id neque. Nam ultricies ac felis ut lobortis. Praesent ac purus vitae est dignissim sollicitudin. Duis iaculis tristique euismod. Nulla tellus libero, gravida sit amet nisi vitae, ultrices venenatis turpis. Morbi ut dui nunc.'}
