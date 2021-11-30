let localVideo = document.getElementById("local-video")
let remoteVideo = document.getElementById("remote-video")


localVideo.style.opacity = 0
remoteVideo.style.opacity = 0

localVideo.onplaying = () => { localVideo.style.opacity = 1 }
remoteVideo.onplaying = () => { remoteVideo.style.opacity = 1 }

let peer
function init(userId) {
//    const Config = {
//        secure: true,
//        port: 443,
//        iceServers: [
//            { url: 'stun:stun.apiprofamilia.com:3478' },
//            { url: 'stun:stun.apiprofamilia.com:5349' },
//            { url: 'turn:turn.apiprofamilia.com:3478?transport=udp', username: 'admin', credential: 'Adivina++' },
//            { url: 'turn:turn.apiprofamilia.com:3478?transport=tcp', username: 'admin', credential: 'Adivina++' },
//        ]
//    };

 peer = new Peer(userId,{
            host: '143.208.181.241',
//            secure:false,
            port: 80,
            path: '/peerjs/videocallapp',
            config: {
                'iceServers': [
                        { url: 'stun:stun.profamiliani.com:3478' },
                        { url: 'stun:stun.profamiliani.com:5349' },
                        { url: 'turn:turn.profamiliani.com:3478?transport=udp', username: 'admin', credential: 'Adivina++' },
                        { url: 'turn:turn.profamiliani.com:3478?transport=tcp', username: 'admin', credential: 'Adivina++' }
                ]
             }
       })

//esta es la config funcional
//      peer = new Peer(userId,{
//            host: 'api-unfpa.herokuapp.com',
//            secure:true,
//            port: 443,
//            path: '/peerjs/videocallapp',
//            config: {
//                'iceServers': [
//                        { url: 'stun:stun.apiprofamilia.com:3478' },
//                        { url: 'stun:stun.apiprofamilia.com:5349' },
//                        { url: 'turn:turn.apiprofamilia.com:3478?transport=udp', username: 'admin', credential: 'Adivina++' },
//                        { url: 'turn:turn.apiprofamilia.com:3478?transport=tcp', username: 'admin', credential: 'Adivina++' }
//                ]
//             }
//       })
//aqui termina la config funcional

//    peer = new Peer(userId, {
////        host: 'apiprofamilia.com"',
////        port: 443,
////        secure:true,
//////      path: '/peerjs/videocallapp',
//
//    })

    peer.on('open', () => {
        Android.onPeerConnected()
    })

    listen()
}

let localStream
function listen() {

    peer.on('call', (call) => {

        navigator.getUserMedia({
            audio: true,
            video: true
        }, (stream) => {
            localVideo.srcObject = stream
            localStream = stream

            call.answer(stream)
            call.on('stream', (remoteStream) => {
                remoteVideo.srcObject = remoteStream
                remoteVideo.className = "primary-video"
                localVideo.className = "secondary-video"
            })

        })

    })
}

function startCall(otherUserId) {
    navigator.getUserMedia({
        audio: true,
        video: true
    }, (stream) => {

        localVideo.srcObject = stream
        localStream = stream

        const call = peer.call(otherUserId, stream)
        call.on('stream', (remoteStream) => {
            remoteVideo.srcObject = remoteStream

            remoteVideo.className = "primary-video"
            localVideo.className = "secondary-video"
        })

    })
}

function toggleVideo(b) {
    if (b == "true") {
        localStream.getVideoTracks()[0].enabled = true
    } else {
        localStream.getVideoTracks()[0].enabled = false
    }
}

function toggleAudio(b) {
    if (b == "true") {
        localStream.getAudioTracks()[0].enabled = true
    } else {
        localStream.getAudioTracks()[0].enabled = false
    }
}