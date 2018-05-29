# ARView 2

## NodeFactory

De NodeFactory is een klasse die Nodes aanmaakt en deze vervolgens renderd op het meegegeven fragment tijdens de initialisatie.

### Initialisatie

``` Java
NodeFactory nodeFactory = new Node(context, arFragment, destination, parentNode);
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
| drawSign(sign)      | Tekenen van aanwijzingsbord op scherm


## ARScene

De ARScene is een fragment die kan ingeladen worden als er weinig aanpassingen moeten worden gemaakt. aan het fragment wordt een lijst van 'Signs' meegegeven en een locatie.

``` Java
Location location = getIntent().getParcelableExtra("location");

ArrayList<Sign> signs = new ArrayList<>();
signs.add(new Sign("Wegwijzer", "sign.sfb", "sign", new Vector3(0.40f, 0.40f, 0.40f), true));
signs.add(new Sign("Wegwijzer groot", "sign.sfb", "sign", new Vector3(0.60f, 0.60f, 0.60f), true));

Bundle bundle = new Bundle();
bundle.putParcelableArrayList("itemList", signs);
bundle.putParcelable("location", location);

ARScene arScene = new ARScene();
arScene.setArguments(bundle);

FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
ft.add(R.id.layout_ar_container, arScene);
ft.commit();
```
