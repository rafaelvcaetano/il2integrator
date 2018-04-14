# Career Service

The career service allows routes of carer missions to be retrieved on demand to be displayed by
third party applications.

This service is only available if the game's binary (named "bin/game/Il-2.exe") is found.

The coordinates of each route point is defined in game world units. The X component increases
horizontally from west to east and the Y component increases vertically from south to north.

## Commands
#### List Campaigns
* Description: Lists all available careers
* Command: `campaigns`
* Defined in class: `ListCampaigns`
* Arguments: none
* Response class: `CampaignList`

#### Load Mission
* Description: Loads the next mission for the given career ID
* Command: `mission`
* Defined in class: `LoadMission`
* Arguments:
    * `campaign` (required) - The ID of the campaign for which to load the mission
* Response class: `Mission`
* Notes: The `route` of the returned `Mission` instance may be `null` when no mission is available.
This may mean that no route is planned for the given career.

#### Start Game
* Description: Launches IL-2 Sturmovik 
* Command: `start`
* Defined in class: `StartGame`
* Arguments: none
* Response class: `Response`