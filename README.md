# ARView 2

## NodeFactory

De NodeFactory is een klasse die Nodes aanmaakt en deze vervolgens renderd op het meegegeven fragment tijdens de initialisatie.

### Initialisatie

``` Java
NodeFactory nodeFactory = new Node(context, arFragment, destination);
```

### Lifecycle

Om het compass te activeren moet resume worden aangeroepen bij het starten van de activity of fragment.

``` Java
factory.pause();
factory.resume();
```

### Methodes

| Methodes        | Doel
| ------------- |:-------------:
| drawArrow()      | Tekenen van pijl op scherm
| drawSign(sign, parentNode)      | Tekenen van aanwijzingsbord op scherm


## ARScene

ARScene is een fragment dat kan worden ingeladen wanneer er weinig aanpassingen moeten worden gemaakt aan de AR view. Het fragment verwacht een lijst van Sign objecten, tussen deze objecten kan de gebruiker vervolgens kiezen.

#### Gebruik fragment

``` Java
Location location = getIntent().getParcelableExtra("location");

Animation animation = DefaultConfig.getDefaultAnimation();
SignText text = DefaultConfig.getDefaultText();
ArrayList<Sign> signs = DefaultConfig.getDefaultSignList();

//Set animation and text on sign small
signs.get(0).setAnimation(animation);
signs.get(0).setSignText(text);

Bundle bundle = new Bundle();
bundle.putParcelableArrayList("signs", signs);
bundle.putParcelable("location", location);

ARScene arScene = new ARScene();
arScene.setArguments(bundle);

FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
ft.add(R.id.layout_ar_container, arScene);
ft.commit();
```

## Klasse

### Sign

| Variable        | Doel
| ------------- |:-------------:
| name     | Naam die wordt weergegeven in de lijst
| sfbPath      | Path naar Sfb file
| imagePath     | Path naar imagefile lijst
| scale     | Vector3 scaling sign
| animation      | Animation object
| signText      | SignText object

### SignText

| Variable        | Doel
| ------------- |:-------------:
| text     | Text die wordt weergegeven op sign
| scale      | Vector3 scaling text
| position     | Vector3 positie text
| positionReflected     | Positie tekst hoek bord > 180 & < 360

### Animation

| Variable        | Doel
| ------------- |:-------------:
| sfbPath    | Path naar sfb voor object
| durationMilliseconds      | Tijd volledige animatie
| position     | Vector3 positie relatief tegenover sign
| scale     | Vector3 scaling van object

## DefaultConfig

DefaultConfig is een klasse met een aantal static functies die objecten terug geven die juist zijn ingesteld zodat deze meteen zijn te gebruiken.

| Methodes        | Doel
| ------------- |:-------------:
| getDefaultAnimation()     | Geeft een cirkel animatie terug
| getDefaultSignSmall()      | Geeft een kleine sign terug
| getDefaultSignBig()     | Geeft een kleine sign terug
| getDefaultSignList()     | Geeft een lijst terug met de voorgaande signs
| getDefaultText()      | Weergeeft het aantal meter op de signs
