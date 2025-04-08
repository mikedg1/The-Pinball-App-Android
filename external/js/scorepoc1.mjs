// Just words
import {config} from "dotenv"
import OpenAI from "openai"
import fs from "fs"

config() // Add this at the top of your file

const openai = new OpenAI({
    apiKey: process.env.OPENAI_API_KEY, // This will now read from .env file
})

// const pairs = JSON.parse(fs.readFileSync("pairs.json", "utf-8"));
const pairs = fs.readFileSync("pairs.csv", "utf-8")
// CSV worked good for getting ID
// Not the exact name

async function getScoreInfo() {
    const base64Image = fs.readFileSync("Venom.jpg", "base64");

    try {
        const completion = await openai.responses.create(
            {
                "model": "gpt-4o",
                "input": [
                    {
                        "role": "system",
                        "content": "Taking an image as input, call the function with the required parameters. Use the below machineName,opdbId pairs to find the machine_name and opdb_id. If you find a matching opdb_id, it should use the matching machine_name. :\nChallenger,G50L9-MDxXD\nMoon Shot,GR7V3-MQPyL\nGalaxy Play,GRDlQ-MJ9yJ\nFree Fall,G4xlK-MDEKL\nThe 30's,G4Xdq-MLBlD\nSisters,G5WPp-MLyZJ\nSpace Mission,GRnX6-MQk8L\nGroovy,GRpKY-MDlYQ\nBig Ben,G5QBX-MQd1L\nCity Slicker,GrEVb-MLOxJ\nBig Day,G48JX-MJw3J\nFlash Gordon,G5728-MDbjD\nPinball Champ '82,GrPdq-MQYkJ\nSurfer,GrZwo-MJNlQ\nHigh Roller Casino,G4JZ4-ML0pL\nExplorer,G5VPX-MQjXJ\nThe 4 Horsemen,GRKdD-MLnxD\nBlondie,GRVdN-MLqwD\nSky·Line,GRQ6d-MDRlL\nDealer's Choice,GRwW9-MJoeQ\nApollo 13,G411e-MJrEL\nCriterium 75,G4Plz-MDz6D\nMoon Flight,GreN3-MJ7wJ\n\n"
                    },
                    {
                        "role": "user",
                        "content": [
                            {
                                "type": "input_text",
                                "text": "What pinball machine and score are in this image?"
                            },
                            {
                                "type": "input_image",
                                "image_url": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAQAAAAEACAIAAADTED8xAAADMElEQVR4nOzVwQnAIBQFQYXff81RUkQCOyDj1YOPnbXWPmeTRef+/3O/OyBjzh3CD95BfqICMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMK0CMO0TAAD//2Anhf4QtqobAAAAAElFTkSuQmCC"
                            }
                        ]
                    }
                ],
                "text": {
                    "format": {
                        "type": "json_schema",
                        "name": "store_pinball_data",
                        "description": "Stores data read from an image of a pinball machine including machine name, opdb id, and possible player scores",
                        "strict": true,
                        "schema": {
                            "type": "object",
                            "required": [
                                "machine_name",
                                "opdb_id",
                                "player_scores"
                            ],
                            "properties": {
                                "opdb_id": {
                                    "type": "string",
                                    "description": "Unique identifier for the machine in the OPDB"
                                },
                                "machine_name": {
                                    "type": "string",
                                    "description": "Name of the pinball machine in the OPDB"
                                },
                                "player_scores": {
                                    "type": "array",
                                    "description": "List of scores for the players",
                                    "items": {
                                        "type": "number",
                                        "description": "Score of a player"
                                    }
                                }
                            },
                            "additionalProperties": false
                        }
                    }
                }
            }



            // Below is working
        //     {
        //     model: "gpt-4o",
        //     input: [
        //         {
        //             role: "system",
        //             content: `Taking an image as input, call the function with the required parameters. Use the below machineName,opdbId pairs to find the machine_name and opdb_id. If you find a matching opdb_id, it should use the matching machine_name. :\n${pairs}`,
        //         },
        //         {
        //             role: "user",
        //             content: [
        //                 { type: "input_text", text: "What pinball machine and score are in this image?" },
        //                 {
        //                     type: "input_image",
        //                     image_url: `data:image/jpeg;base64,${base64Image}`,
        //                 },
        //             ]
        //
        //         },
        //     ],
        //     text: {
        //         format: {
        //             type: "json_schema",
        //             "name": "store_pinball_data",
        //             "description": "Stores data read from an image of a pinball machine including machine name, opdb id, and possible player scores",
        //             "strict": true,
        //             "schema": {
        //                 "type": "object",
        //                 "required": [
        //                     "machine_name",
        //                     "opdb_id",
        //                     "player_scores"
        //                 ],
        //                 "properties": {
        //                     "opdb_id": {
        //                         "type": "string",
        //                         "description": "Unique identifier for the machine in the OPDB"
        //                     },
        //                     "machine_name": {
        //                         "type": "string",
        //                         "description": "Name of the pinball machine in the OPDB"
        //                     },
        //                     "player_scores": {
        //                         "type": "array",
        //                         "items": {
        //                             "type": "number",
        //                             "description": "Score of a player"
        //                         },
        //                         "description": "List of scores for the players"
        //                     }
        //                 },
        //                 "additionalProperties": false
        //             }
        //         }
        //     }
        // }
        )
        console.log(completion)
    } catch (error) {
        console.error("Error:", error)
    }
}

// Call the function
await getScoreInfo()