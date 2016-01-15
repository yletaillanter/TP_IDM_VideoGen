package app

import java.io.BufferedWriter
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import org.eclipse.emf.common.util.URI
import org.xtext.example.videogen.yogo.Alternatives
import org.xtext.example.videogen.yogo.Mandatory
import org.xtext.example.videogen.yogo.Optional
import playlist.Playlist
import playlist.PlaylistFactory
import playlist.Video
import java.io.BufferedReader
import java.io.InputStreamReader

class videoGenerator {
	
	private var util = new Util
	var Runtime rt = Runtime.getRuntime()
	
	def makeFfmpegList(){
		var videoGen = util.loadVideoGenerator(URI.createURI("foo.yogo"))

		// On recup la premiere video et la CAST en mandatory
		var vidMandatory = videoGen.videoseqs.get(0) as Mandatory;		
		var result = "file '" + vidMandatory.desc.path +"'";
		
		// Video Optional
		if(util.getOptional) {
			var vidOptional = videoGen.videoseqs.get(1) as Optional;
			result+= "\n"
			result+= "file '" + vidOptional.desc.path +"'";
		}
		
		//Video alternative
		var vidAlternatives = videoGen.videoseqs.get(2) as Alternatives
		var nombreAleatoire = util.randomWithRange(0, vidAlternatives.videoDescriptions.length-1) as int
		result+= "\n"
		result+= "file '" + vidAlternatives.videoDescriptions.get(nombreAleatoire).path
								
		// Video Optional
		if(util.getOptional) {
			var vidOptional = videoGen.videoseqs.get(3) as Optional;
			result+= "\n"
			result+= "file '" + vidOptional.desc.path +"'";
		}	
		
		// On recup la premiere video et la CAST en mandatory
		var vidMandatory2 = videoGen.videoseqs.get(4) as Mandatory;
		result+= '\n'
		result+= "file '" + vidMandatory2.desc.path +"'";		
								
		System.out.println(result)
	}
	
	
	def proceduralFfmpegList() {
		
		var videoGen = util.loadVideoGenerator(URI.createURI("foo.yogo"))
		var nbVideos = videoGen.videoseqs.length;
		
		var result = "# FFMPEG LIST";
		result+= '\n' 
		
		for (var i =0; i<nbVideos; i++) {
			var type = videoGen.videoseqs.get(i).eClass.name
			if (type.equals("Mandatory")){
				var video = videoGen.videoseqs.get(i) as Mandatory;		
				result+= "file '" + video.desc.path +"'";
				result+= "\n"
			}
			else if (type.equals("Optional")){
				if(util.getOptional) {
					var vidOptional = videoGen.videoseqs.get(i) as Optional;
					result+= "file '" + vidOptional.desc.path +"'";
					result+= "\n"
				}
			}
			else {
				var vidAlternatives = videoGen.videoseqs.get(i) as Alternatives
				var nombreAleatoire = util.randomWithRange(0, vidAlternatives.videoDescriptions.length-1) as int
				result+= "file '" + vidAlternatives.videoDescriptions.get(nombreAleatoire).path
				result+= "\n"				
			}
		}
		System.out.println(result)
	}
	
	def modelToModelProceduralFfmpegList() {
				
		var videoGen = util.loadVideoGenerator(URI.createURI("foo.yogo"))
		var nbVideos = videoGen.videoseqs.size
		var factory = PlaylistFactory.eINSTANCE
		var mPlaylistImpl = factory.createPlaylist as Playlist
		
		
		for (var i =0; i<nbVideos; i++) {
			var type = videoGen.videoseqs.get(i) //.eClass.name
				
			if (type instanceof Mandatory) { 
				var video = factory.createVideo as Video
				var videoMandatory = videoGen.videoseqs.get(i) as Mandatory
				
				video.path = videoMandatory.desc.path
				//video.duration = videoMandatory.desc.duration
				
				mPlaylistImpl.video.add(video)
			}
			else if (type instanceof Optional){
				if(util.getOptional) {
					var video2 = factory.createVideo 
					var videoOptional = videoGen.videoseqs.get(i) as Optional
					
					video2.path = videoOptional.desc.path
					//video2.duration = videoOptional.desc.duration
					
					mPlaylistImpl.video.add(video2)
				}
			}
			else { // case of alternatives
				var video3 = factory.createVideo 
				var videoAlternatives = videoGen.videoseqs.get(i) as Alternatives
				
				var nombreAleatoire = util.randomWithRange(0, videoAlternatives.videoDescriptions.length-1) as int
				
				video3.path = videoAlternatives.videoDescriptions.get(nombreAleatoire).path
				//video3.duration = videoAlternatives.videoDescriptions.get(nombreAleatoire).duration
				
				mPlaylistImpl.video.add(video3)				
			}
		}
		System.out.println("##FFMPEG")
		mPlaylistImpl.video.forEach[vid | System.out.println(vid.path)]
	}

	def m3uGenerator() {
		var videoGen = util.loadVideoGenerator(URI.createURI("foo.yogo"))
		var nbVideos = videoGen.videoseqs.size
		var factory = PlaylistFactory.eINSTANCE
		var mPlaylistImpl = factory.createPlaylist as Playlist
		var writer =  new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/myPlaylist.m3u"), "utf-8"))
		
		writer.write("#EXTM3U")
		writer.write("\n")
				
		for (var i =0; i<nbVideos; i++) {
			var type = videoGen.videoseqs.get(i) //.eClass.name
				
			if (type instanceof Mandatory) { 
				var video = factory.createVideo as Video
				var videoMandatory = videoGen.videoseqs.get(i) as Mandatory
						
				video.path = videoMandatory.desc.path
				video.duration = util.getDuration(video.path)
				video.description = videoMandatory.desc.description
			    				
				mPlaylistImpl.video.add(video)
			}
			else if (type instanceof Optional){
				if(util.getOptional) {
					var video = factory.createVideo 
					var videoOptional = videoGen.videoseqs.get(i) as Optional
					
					video.path = videoOptional.desc.path
					video.duration = util.getDuration(video.path)
					video.description = videoOptional.desc.description
					
				    mPlaylistImpl.video.add(video)
				}
			}
			else { // case of alternatives
				var video = factory.createVideo 
				var videoAlternatives = videoGen.videoseqs.get(i) as Alternatives
				var nombreAleatoire = util.randomWithRange(0, videoAlternatives.videoDescriptions.length-1) as int
				
				video.path = videoAlternatives.videoDescriptions.get(nombreAleatoire).path
				video.duration = util.getDuration(video.path)
				video.description = videoAlternatives.videoDescriptions.get(nombreAleatoire).description
								 
				mPlaylistImpl.video.add(video)				
			}
		}
		
		
		for (Video vid :mPlaylistImpl.video){
			try {
				writer.write("#EXT-­X-­DISCONTINUITY")
				writer.write("\n")
				writer.write("#EXTINF:") writer.write(vid.duration) writer.write(",") writer.write(vid.description)
				writer.write("\n")
				writer.write(vid.path)
				writer.write("\n")
			}
			catch(Exception e) { 
				System.out.println(e.getMessage())
			}
			/* 
			System.out.println("#EXT-­X-­DISCONTINUITY")
			System.out.print("#EXTINF:") System.out.print(vid.duration) System.out.print(",") System.out.println(vid.description)
			System.out.println(vid.path)
			System.out.print("#EXT-­X-­ENDLIST")
			*/ 
		}
		writer.write("#EXT-­X-­ENDLIST")
		writer.close()
	}
	
	def videoToTs() {
		var videoGen = util.loadVideoGenerator(URI.createURI("foo.yogo"))
		var nbVideos = videoGen.videoseqs.size
		var factory = PlaylistFactory.eINSTANCE
		var mPlaylistImpl = factory.createPlaylist
		
		for (var i =0; i<nbVideos; i++) {
			var type = videoGen.videoseqs.get(i) //.eClass.name
				
			if (type instanceof Mandatory) { 
				var video = factory.createVideo as Video
				var videoMandatory = videoGen.videoseqs.get(i) as Mandatory
						
				video.path = videoMandatory.desc.path
				video.duration = util.getDuration(video.path)
				video.description = videoMandatory.desc.description
			    				
				mPlaylistImpl.video.add(video)
			}
			else if (type instanceof Optional){
				if(util.getOptional) {
					var video = factory.createVideo 
					var videoOptional = videoGen.videoseqs.get(i) as Optional
					
					video.path = videoOptional.desc.path
					video.duration = util.getDuration(video.path)
					video.description = videoOptional.desc.description
					
				    mPlaylistImpl.video.add(video)
				}
			}
			else { // case of alternatives
				var video = factory.createVideo 
				var videoAlternatives = videoGen.videoseqs.get(i) as Alternatives
				var nombreAleatoire = util.randomWithRange(0, videoAlternatives.videoDescriptions.length-1) as int
				
				video.path = videoAlternatives.videoDescriptions.get(nombreAleatoire).path
				video.duration = util.getDuration(video.path)
				video.description = videoAlternatives.videoDescriptions.get(nombreAleatoire).description
								 
				mPlaylistImpl.video.add(video)				
			}
		}
		
		for (Video vid :mPlaylistImpl.video){
			rt.exec("/usr/local/bin/ffmpeg -i data/"+vid.path+" -c copy -bsf h264_mp4toannexb data/ts/"+vid.path+".ts")
			System.out.println(vid.path)			
		}
	}
	
	def generateVignette(){
		var videoGen = util.loadVideoGenerator(URI.createURI("foo.yogo"))
		
		//check errors
		
		
		var nbVideos = videoGen.videoseqs.size
		var factory = PlaylistFactory.eINSTANCE
		var mPlaylistImpl = factory.createPlaylist
		
		for (var i =0; i<nbVideos; i++) {
			var type = videoGen.videoseqs.get(i) //.eClass.name
				
			if (type instanceof Mandatory) { 
				var video = factory.createVideo as Video
				var videoMandatory = videoGen.videoseqs.get(i) as Mandatory
						
				video.path = videoMandatory.desc.path
				video.duration = util.getDuration(video.path)
				video.description = videoMandatory.desc.description
			    				
				mPlaylistImpl.video.add(video)
			}
			else if (type instanceof Optional){
				if(util.getOptional) {
					var video = factory.createVideo 
					var videoOptional = videoGen.videoseqs.get(i) as Optional
					
					video.path = videoOptional.desc.path
					video.duration = util.getDuration(video.path)
					video.description = videoOptional.desc.description
					
				    mPlaylistImpl.video.add(video)
				}
			}
			else { // case of alternatives
				var video = factory.createVideo 
				var videoAlternatives = videoGen.videoseqs.get(i) as Alternatives
				var nombreAleatoire = util.randomWithRange(0, videoAlternatives.videoDescriptions.length-1) as int
				
				video.path = videoAlternatives.videoDescriptions.get(nombreAleatoire).path
				video.duration = util.getDuration(video.path)
				video.description = videoAlternatives.videoDescriptions.get(nombreAleatoire).description
								 
				mPlaylistImpl.video.add(video)				
			}
		}
		
		for (Video vid :mPlaylistImpl.video){
			rt.exec("/usr/local/bin/ffmpeg -y -i data/" + vid.path + " -r 1 -t 00:00:01 -ss 00:00:02 -f image2 data/vignette/" + vid.path + ".png")
			System.out.println(vid.path)
		}
	}
	
	def errorTest() {
		var videoGen = util.loadVideoGenerator(URI.createURI("foo.yogo"))
		util.checkErrors(videoGen)
	}
}