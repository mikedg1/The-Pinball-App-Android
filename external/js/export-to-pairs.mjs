import fs from 'fs';

// Read the opdbexport.json file
fs.readFile('opdbexport.json', 'utf8', (err, data) => {
    if (err) {
        console.error('Error reading opdbexport.json:', err);
        return;
    }

    try {
        // Parse the JSON data
        const opdbData = JSON.parse(data);

        // Extract machine names and their corresponding opdbId
        const pairs = opdbData.map((entry) => ({
            machineName: entry.name,
            opdbId: entry.opdb_id,
        }));

        // Write the pairs array to pairs.json
        fs.writeFile('pairs.json', JSON.stringify(pairs, null, 2), (err) => {
            if (err) {
                console.error('Error writing pairs.json:', err);
                return;
            }

            console.log('pairs.json has been successfully created.');
        });
        fs.writeFile('pairs.csv', opdbData.map((entry) => `${entry.name},${entry.opdb_id}`).join('\n'), (err) => {
            if (err) {
                console.error('Error writing pairs.json:', err);
                return;
            }

            console.log('pairs.json has been successfully created.');
        });

    } catch (parseError) {
        console.error('Error parsing opdbexport.json:', parseError);
    }
});