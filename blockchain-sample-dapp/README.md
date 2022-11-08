## NCP Blockchain Service Sample DApplication (Tracking service)

### requirement
- node 12 version 이상
- npm 6 version 이상

### setting connection profile & user wallet
- /backend/wallet 폴더에 아래 두개 파일을 NCP Blockchain Service 에서 다운로드 받아서 저장
   - connection_property.json (MSP의 접속정보)
   - client.json (client 인증서)
- /backend/wallet/wallet.js 에서 채널명을 설정한다 (10 line)

### Run 
```bash
# install dependencies
cd frontend 
npm install
cd ../backend
npm install

# build frontend
cd frontend
npm run build

## run backend
cd ../backend
./start.sh

```
접속 http://localhost:3000/
