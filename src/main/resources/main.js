function maxServer(spots, servers) {
    var spotsCapacity = [10, 5, 5, 2, 1]; // max size available
    var server = [ 0, 1, 2, 3 ,4 , 5 ]; // servers
    var getMax = 0;
    
    
    for( i=0; i < spotsCapacity.length; i++){

        if(spotsCapacity[i] > server[i])
        {
            getMax = Math.max(spotsCapacity[i], server[i]);
        }
        console.log("assign to max ", getMax);
    }
    return getMax;
}


function allocateServerInFarm(datafarm, servers) {
	
	return new com.reply.spartans.datacenter.model.Solution(null);
}